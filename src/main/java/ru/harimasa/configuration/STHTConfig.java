package ru.harimasa.configuration;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.text.DecimalFormat;

public class STHTConfig {
    public static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    public static final ConfigClassHandler<BasicConfig> CONFIG = ConfigClassHandler.createBuilder(BasicConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("stht.json"))
                    .setJson5(true)
                    .build())
            .build();

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.translatable("stht.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("stht.category.main"))
                        // Main
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("stht.category.main"))
                                .option(Option.<Boolean>createBuilder() // is Enabled?
                                        .name(Text.translatable("stht.main.isEnabled"))
                                        .description(OptionDescription.of(Text.translatable("stht.main.isEnabled.desc")))
                                        .binding(defaults.isEnabled, () -> config.isEnabled, newVal -> config.isEnabled = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt)
                                                .onOffFormatter()
                                                .coloured(true))
                                        .build())
                                .option(Option.<Float>createBuilder() // Delay
                                        .name(Text.translatable("stht.main.delay"))
                                        .description(OptionDescription.of(Text.translatable("stht.main.delay.desc")))
                                        .binding(0.3f, () -> config.delay, newVal -> config.delay = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .formatValue(value -> Text.literal(FORMAT.format(value)))
                                                .range(0.0f, 5f)
                                                .step(0.01f))
                                        .build())
                                .option(Option.<Boolean>createBuilder() // is Usage Enchanted Totems?
                                        .name(Text.translatable("stht.main.isUsageEnchantedTotems"))
                                        .description(OptionDescription.of(Text.translatable("stht.main.isUsageEnchantedTotems.desc")))
                                        .binding(defaults.isUsageEnchantedTotems, () -> config.isUsageEnchantedTotems, newVal -> config.isUsageEnchantedTotems = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt)
                                                .onOffFormatter()
                                                .coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder() // is Usage Renamed Totems?
                                        .name(Text.translatable("stht.main.isUsageRenamedTotems"))
                                        .description(OptionDescription.of(Text.translatable("stht.main.isUsageRenamedTotems.desc")))
                                        .binding(defaults.isUsageRenamedTotems, () -> config.isUsageRenamedTotems, newVal -> config.isUsageRenamedTotems = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt)
                                                .onOffFormatter()
                                                .coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder() // is Ignore Item In Secondary Hand?
                                        .name(Text.translatable("stht.main.isIgnoreItemInSecondaryHand"))
                                        .description(OptionDescription.of(Text.translatable("stht.main.isIgnoreItemInSecondaryHand.desc")))
                                        .binding(defaults.isIgnoreItemInSecondaryHand, () -> config.isIgnoreItemInSecondaryHand, newVal -> config.isIgnoreItemInSecondaryHand = newVal)
                                        .controller(opt -> BooleanControllerBuilder.create(opt)
                                                .onOffFormatter()
                                                .coloured(true))
                                        .build())
                                .build())
                        .build()))).generateScreen(parent);
    }
}