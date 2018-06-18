package net.minecraft.creativetab;

import javax.annotation.Nullable;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

public abstract class CreativeTabs {

    public static final CreativeTabs[] CREATIVE_TAB_ARRAY = new CreativeTabs[12];
    public static final CreativeTabs BUILDING_BLOCKS = new CreativeTabs(0, "buildingBlocks") {
    };
    public static final CreativeTabs DECORATIONS = new CreativeTabs(1, "decorations") {
    };
    public static final CreativeTabs REDSTONE = new CreativeTabs(2, "redstone") {
    };
    public static final CreativeTabs TRANSPORTATION = new CreativeTabs(3, "transportation") {
    };
    public static final CreativeTabs MISC = new CreativeTabs(6, "misc") {
    };
    public static final CreativeTabs SEARCH = (new CreativeTabs(5, "search") {
    }).setBackgroundImageName("item_search.png");
    public static final CreativeTabs FOOD = new CreativeTabs(7, "food") {
    };
    public static final CreativeTabs TOOLS = (new CreativeTabs(8, "tools") {
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] { EnumEnchantmentType.ALL, EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE});
    public static final CreativeTabs COMBAT = (new CreativeTabs(9, "combat") {
    }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] { EnumEnchantmentType.ALL, EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_CHEST, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON, EnumEnchantmentType.WEARABLE, EnumEnchantmentType.BREAKABLE});
    public static final CreativeTabs BREWING = new CreativeTabs(10, "brewing") {
    };
    public static final CreativeTabs MATERIALS = CreativeTabs.MISC;
    public static final CreativeTabs HOTBAR = new CreativeTabs(4, "hotbar") {
    };
    public static final CreativeTabs INVENTORY = (new CreativeTabs(11, "inventory") {
    }).setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
    private final int tabIndex;
    private final String tabLabel;
    private String backgroundTexture = "items.png";
    private boolean hasScrollbar = true;
    private boolean drawTitle = true;
    private EnumEnchantmentType[] enchantmentTypes = new EnumEnchantmentType[0];
    private ItemStack iconItemStack;

    public CreativeTabs(int i, String s) {
        this.tabIndex = i;
        this.tabLabel = s;
        this.iconItemStack = ItemStack.EMPTY;
        CreativeTabs.CREATIVE_TAB_ARRAY[i] = this;
    }

    public CreativeTabs setBackgroundImageName(String s) {
        this.backgroundTexture = s;
        return this;
    }

    public CreativeTabs setNoTitle() {
        this.drawTitle = false;
        return this;
    }

    public CreativeTabs setNoScrollbar() {
        this.hasScrollbar = false;
        return this;
    }

    public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
        return this.enchantmentTypes;
    }

    public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType... aenchantmentslottype) {
        this.enchantmentTypes = aenchantmentslottype;
        return this;
    }

    public boolean hasRelevantEnchantmentType(@Nullable EnumEnchantmentType enchantmentslottype) {
        if (enchantmentslottype != null) {
            EnumEnchantmentType[] aenchantmentslottype = this.enchantmentTypes;
            int i = aenchantmentslottype.length;

            for (int j = 0; j < i; ++j) {
                EnumEnchantmentType enchantmentslottype1 = aenchantmentslottype[j];

                if (enchantmentslottype1 == enchantmentslottype) {
                    return true;
                }
            }
        }

        return false;
    }
}
