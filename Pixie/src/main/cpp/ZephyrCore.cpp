#include <jni.h>
#include <android/log.h>
#include <EGL/egl.h>
#include <GLES3/gl3.h>

#include "imgui.h"
#include "backends/imgui_impl_opengl3.h"
#include "dobby.h"
#include "xdl.h"

#define LOG_TAG "ZephyrCore"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

static EGLBoolean (*orig_eglSwapBuffers)(EGLDisplay dpy, EGLSurface surface) = nullptr;
static bool g_ImGuiInitialized = false;
static bool g_ShowMenu = true;

EGLBoolean hook_eglSwapBuffers(EGLDisplay dpy, EGLSurface surface) {
    if (!g_ImGuiInitialized) {
        IMGUI_CHECKVERSION();
        ImGui::CreateContext();
        ImGuiIO& io = ImGui::GetIO();
        io.IniFilename = nullptr; // Avoid crashes on read-only directories

        EGLint width = 0, height = 0;
        eglQuerySurface(dpy, surface, EGL_WIDTH, &width);
        eglQuerySurface(dpy, surface, EGL_HEIGHT, &height);
        if (width <= 0) width = 1920;
        if (height <= 0) height = 1080;
        io.DisplaySize = ImVec2((float)width, (float)height);

        // Mobile touch friendly style
        ImGuiStyle& style = ImGui::GetStyle();
        style.ScaleAllSizes(2.0f);
        io.FontGlobalScale = 1.6f;
        ImGui::StyleColorsDark();

        ImGui_ImplOpenGL3_Init("#version 300 es");
        g_ImGuiInitialized = true;
        LOGI("ImGui OpenGL3 initialized successfully for surface (%dx%d)", width, height);
    }

    // Dynamic resolution / orientation update
    EGLint curWidth = 0, curHeight = 0;
    eglQuerySurface(dpy, surface, EGL_WIDTH, &curWidth);
    eglQuerySurface(dpy, surface, EGL_HEIGHT, &curHeight);
    if (curWidth > 0 && curHeight > 0) {
        ImGui::GetIO().DisplaySize = ImVec2((float)curWidth, (float)curHeight);
    }

    ImGui_ImplOpenGL3_NewFrame();
    ImGui::NewFrame();

    if (g_ShowMenu) {
        ImGui::SetNextWindowSize(ImVec2(520.0f, 320.0f), ImGuiCond_FirstUseEver);
        ImGui::SetNextWindowPos(ImVec2(60.0f, 60.0f), ImGuiCond_FirstUseEver);

        if (ImGui::Begin("ZephyrMC | Minecraft Bedrock 1.21.51.1", &g_ShowMenu)) {
            ImGui::TextColored(ImVec4(0.2f, 1.0f, 0.4f, 1.0f), "Status: In-Process Hook ACTIVE");
            ImGui::Text("FPS: %.1f (%.2f ms)", ImGui::GetIO().Framerate, 1000.0f / (ImGui::GetIO().Framerate > 0 ? ImGui::GetIO().Framerate : 60.0f));
            ImGui::Separator();

            static bool showDemo = false;
            ImGui::Checkbox("Show ImGui Demo Window", &showDemo);
            if (showDemo) {
                ImGui::ShowDemoWindow(&showDemo);
            }

            ImGui::Spacing();
            ImGui::Text("Client Modules (Atlas Style):");
            static bool modFps = true;
            static bool modKeystrokes = false;
            static bool modZoom = false;
            ImGui::Checkbox("FPS & 1% Lows HUD", &modFps);
            ImGui::Checkbox("Keystrokes & CPS", &modKeystrokes);
            ImGui::Checkbox("OptiFine Zoom", &modZoom);

            ImGui::Spacing();
            if (ImGui::Button("Minimize Menu")) {
                g_ShowMenu = false;
            }
        }
        ImGui::End();
    } else {
        // Floating button to reopen the menu
        ImGui::SetNextWindowPos(ImVec2(10.0f, 10.0f), ImGuiCond_Always);
        ImGui::SetNextWindowSize(ImVec2(140.0f, 50.0f), ImGuiCond_Always);
        ImGui::Begin("##ReopenZephyr", nullptr, ImGuiWindowFlags_NoDecoration | ImGuiWindowFlags_NoBackground);
        if (ImGui::Button("Open Zephyr", ImVec2(130.0f, 40.0f))) {
            g_ShowMenu = true;
        }
        ImGui::End();
    }

    ImGui::Render();
    ImGui_ImplOpenGL3_RenderDrawData(ImGui::GetDrawData());

    return orig_eglSwapBuffers(dpy, surface);
}

// JNI Touch Input handler
extern "C" JNIEXPORT jboolean JNICALL
Java_com_project_zephyr_client_NativeBridge_onTouchEvent(
    JNIEnv* env, jclass clazz, jint action, jfloat x, jfloat y) {
    if (!g_ImGuiInitialized) {
        return JNI_FALSE;
    }

    ImGuiIO& io = ImGui::GetIO();
    io.AddMousePosEvent(x, y);

    if (action == 0) { // ACTION_DOWN
        io.AddMouseButtonEvent(0, true);
    } else if (action == 1 || action == 3) { // ACTION_UP or ACTION_CANCEL
        io.AddMouseButtonEvent(0, false);
    }

    return io.WantCaptureMouse ? JNI_TRUE : JNI_FALSE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_org_muffin_imgui_NativeBridge_onTouchEvent(
    JNIEnv* env, jclass clazz, jint action, jfloat x, jfloat y) {
    return Java_com_project_zephyr_client_NativeBridge_onTouchEvent(env, clazz, action, x, y);
}

static void InstallHooks() {
    LOGI("Installing eglSwapBuffers hook...");
    void* sym = DobbySymbolResolver("libEGL.so", "eglSwapBuffers");
    if (!sym) {
        void* hEgl = xdl_open("libEGL.so", 0);
        if (hEgl) {
            sym = xdl_sym(hEgl, "eglSwapBuffers", nullptr);
            xdl_close(hEgl);
        }
    }

    if (sym) {
        int hookRet = DobbyHook(sym, (dobby_dummy_func_t)hook_eglSwapBuffers, (dobby_dummy_func_t*)&orig_eglSwapBuffers);
        LOGI("DobbyHook eglSwapBuffers result: %d at %p", hookRet, sym);
    } else {
        LOGE("Failed to locate eglSwapBuffers symbol in libEGL.so!");
    }
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    LOGI("JNI_OnLoad called for ZephyrMC / Pixie");
    InstallHooks();
    return JNI_VERSION_1_6;
}
