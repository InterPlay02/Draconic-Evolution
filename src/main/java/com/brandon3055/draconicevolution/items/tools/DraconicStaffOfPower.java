package com.brandon3055.draconicevolution.items.tools;

import com.brandon3055.brandonscore.lib.PairKV;
import com.brandon3055.brandonscore.registry.Feature;
import com.brandon3055.draconicevolution.api.IReaperItem;
import com.brandon3055.draconicevolution.api.itemconfig.DoubleConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.IItemConfigField;
import com.brandon3055.draconicevolution.api.itemconfig.ItemConfigFieldRegistry;
import com.brandon3055.draconicevolution.api.itemconfig.ToolConfigHelper;
import com.brandon3055.draconicevolution.api.itemupgrade.UpgradeHelper;
import com.brandon3055.draconicevolution.client.model.tool.ToolOverrideList;
import com.brandon3055.draconicevolution.items.ToolUpgrade;
import com.brandon3055.draconicevolution.utils.DETextures;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static com.brandon3055.draconicevolution.client.model.tool.ToolTransforms.STAFF_STATE;

/**
 * Created by brandon3055 on 5/06/2016.
 */
public class DraconicStaffOfPower extends MiningToolBase implements IAOEWeapon, IReaperItem {
    public DraconicStaffOfPower() {
        super(ToolStats.DRA_STAFF_ATTACK_DAMAGE, ToolStats.DRA_STAFF_ATTACK_SPEED, PICKAXE_OVERRIDES);
        this.baseMiningSpeed = (float) ToolStats.DRA_STAFF_MINING_SPEED;
        this.baseAOE = ToolStats.BASE_DRACONIC_MINING_AOE + 1;
        setEnergyStats(ToolStats.DRACONIC_BASE_CAPACITY * 3, 16000000, 0);
//        this.setHarvestLevel("all", 10);
        this.setHarvestLevel("pickaxe", 10);
        this.setHarvestLevel("axe", 10);
        this.setHarvestLevel("shovel", 10);
    }

    @Override
    public List<String> getValidUpgrades(ItemStack stack) {
        List<String> list = super.getValidUpgrades(stack);
        list.add(ToolUpgrade.ATTACK_DAMAGE);
        list.add(ToolUpgrade.ATTACK_AOE);
        return list;
    }

    @Override
    public int getMaxUpgradeLevel(ItemStack stack, String upgrade) {
        return 3;
    }

    @Override
    public int getToolTier(ItemStack stack) {
        return 2;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (getDisabledEnchants(stack).containsKey(enchantment)) {
            return false;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.type == EnumEnchantmentType.WEAPON;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    //region Attack Stats

    protected double getMaxAttackAOE(ItemStack stack) {
        int level = UpgradeHelper.getUpgradeLevel(stack, ToolUpgrade.ATTACK_AOE);
        if (level == 0) return 2;
        else if (level == 1) return 3;
        else if (level == 2) return 5;
        else if (level == 3) return 8;
        else if (level == 4) return 15;
        else return 0;
    }

    @Override
    public ItemConfigFieldRegistry getFields(ItemStack stack, ItemConfigFieldRegistry registry) {
        registry.register(stack, new DoubleConfigField("attackAOE", getMaxAttackAOE(stack), 0, getMaxAttackAOE(stack), "config.field.attackAOE.description", IItemConfigField.EnumControlType.SLIDER));
        return super.getFields(stack, registry);
    }

    @Override
    public double getWeaponAOE(ItemStack stack) {
        return ToolConfigHelper.getDoubleField("attackAOE", stack);
    }

    //endregion

    @Override
    public int getReaperLevel(ItemStack stack) {
        return 3;
    }

    //region Rendering

    @Override
    public void registerRenderer(Feature feature) {
        super.registerRenderer(feature);
        ToolOverrideList.putOverride(this, DraconicStaffOfPower::handleTransforms);
    }

    @SideOnly (Side.CLIENT)//Avoids synthetic lambda creation booping the classloader on the server.
    private static IModelState handleTransforms(TransformType transformType, IModelState state) {
        return transformType == TransformType.FIXED || transformType == TransformType.GROUND ? STAFF_STATE : state;
    }

    @Override
    public PairKV<TextureAtlasSprite, ResourceLocation> getModels(ItemStack stack) {
        return new PairKV<>(DETextures.DRACONIC_STAFF_OF_POWER, new ResourceLocation("draconicevolution", "models/item/tools/draconic_staff_of_power.obj"));
    }

    //endregion
}
