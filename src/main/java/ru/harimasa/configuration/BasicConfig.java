package ru.harimasa.configuration;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class BasicConfig {

    @SerialEntry
    public boolean isEnabled = true;

    @SerialEntry
    public float delay = 0.3f;

    @SerialEntry
    public boolean isUsageEnchantedTotems = true;

    @SerialEntry
    public boolean isUsageRenamedTotems = true;

    @SerialEntry
    public boolean isIgnoreItemInSecondaryHand = true;
}