package com.vbazh.translater_yandex.utils;

import com.vbazh.translater_yandex.model.Translation;

/**
 *
 */

public class OpenTranslateEvent {
    public final Translation translation;
    public OpenTranslateEvent(Translation translation) {
        this.translation = translation;
    }
}