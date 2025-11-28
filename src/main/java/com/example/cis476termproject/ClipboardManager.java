package com.example.cis476termproject;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClipboardManager {
    private static final java.time.Duration CLEAR_DELAY = java.time.Duration.ofSeconds(30);
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "clipboard-clear-task");
        thread.setDaemon(true);
        return thread;
    });

    private static ScheduledFuture<?> clearTask;

    private ClipboardManager() {
    }

    public static void copyFromProxy(SensitiveValueProxy proxy) {
        if (proxy == null) {
            return;
        }
        copyToClipboard(proxy.getValueForCopy());
    }

    public static void copyToClipboard(String value) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(value);
        clipboard.setContent(content);
        scheduleClear();
    }

    private static void scheduleClear() {
        if (clearTask != null) {
            clearTask.cancel(false);
        }
        clearTask = scheduler.schedule(ClipboardManager::clearClipboard, CLEAR_DELAY.toMillis(), TimeUnit.MILLISECONDS);
    }

    public static void clearClipboard() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        clipboard.clear();
    }

}
