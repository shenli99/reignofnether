package com.solegendary.reignofnether.ability.abilities;

import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.unit.UnitAction;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.villagers.EvokerUnit;
import com.solegendary.reignofnether.util.MyRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;// I18n

import java.util.List;

public class SetFangsCircle extends Ability {

    public static final int CD_MAX_SECONDS = 8;

    private final EvokerUnit evokerUnit;

    public SetFangsCircle(EvokerUnit evokerUnit) {
        super(
            UnitAction.SET_FANGS_CIRCLE,
            CD_MAX_SECONDS * ResourceCost.TICKS_PER_SECOND,
            EvokerUnit.FANGS_RANGE_CIRCLE,
            0,
            true
        );
        this.evokerUnit = evokerUnit;
    }

    @Override
    public AbilityButton getButton(Keybinding hotkey) {
        return new AbilityButton(
            Component.translatable("ability.evoker_fangs_circle.name").getString(),
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/items/shears.png"),
            hotkey,
            () -> !evokerUnit.isUsingLineFangs,
            () -> false,
            () -> true,
            () -> UnitClientEvents.sendUnitCommand(UnitAction.SET_FANGS_CIRCLE),
            null,
            List.of(
                FormattedCharSequence.forward(Component.translatable("ability.evoker_fangs_circle.name").getString(), Style.EMPTY.withBold(true)),
                FormattedCharSequence.forward(
                    "\uE006  " + EvokerUnit.FANGS_DAMAGE * 2 + "  " + "\uE004  " + CD_MAX_SECONDS + "s  \uE005  " + EvokerUnit.FANGS_RANGE_CIRCLE,
                    MyRenderer.iconStyle
                ),
                FormattedCharSequence.forward(Component.translatable("ability.evoker_fangs_circle.description.line1").getString(), Style.EMPTY),
                FormattedCharSequence.forward(Component.translatable("ability.evoker_fangs_circle.description.line2").getString(), Style.EMPTY)
            ),
            this
        );
    }

    public void setCooldownSingle(int cooldown) {
        super.setCooldown(cooldown);
    }

    @Override
    public void setCooldown(int cooldown) {
        super.setCooldown(cooldown);
        for (Ability ability : this.evokerUnit.getAbilities())
            if (ability instanceof SetFangsLine ab)
                ab.setCooldownSingle(cooldown);
    }

    @Override
    public void use(Level level, Unit unitUsing, BlockPos targetBp) {
        evokerUnit.isUsingLineFangs = false;
    }

    @Override
    public boolean canBypassCooldown() { return true; }

    @Override
    public boolean shouldResetBehaviours() { return false; }
}
