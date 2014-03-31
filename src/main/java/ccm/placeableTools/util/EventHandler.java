package ccm.placeableTools.util;

import ccm.placeableTools.PlaceableTools;
import ccm.placeableTools.block.BucketBlock;
import ccm.placeableTools.block.ToolBlock;
import ccm.placeableTools.block.ToolTE;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.item.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class EventHandler
{
    public static final EventHandler INSTANCE    = new EventHandler();

    private EventHandler() {}

    public void init()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * For placing tools in the world
     */
    @ForgeSubscribe
    public void clickEvent(PlayerInteractEvent event)
    {
        World world = event.entityPlayer.getEntityWorld();

        ItemStack itemStack = event.entityPlayer.getHeldItem();

        if (itemStack == null || event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;

        if (itemStack.getItem() instanceof ItemSign && event.face != 1 && event.face != 0)
        {
            if (world.getBlockId(event.x, event.y, event.z) == ToolBlock.getInstance().blockID)
            {
                ToolTE te = (ToolTE) world.getBlockTileEntity(event.x, event.y, event.z);

                if (te.getStack().getItem() instanceof ItemSword && te.addSign(3 + event.face) != -1)
                {
                    if (!event.entityPlayer.capabilities.isCreativeMode) itemStack.stackSize--;
                    if (!world.isRemote) PacketDispatcher.sendPacketToAllInDimension(te.getDescriptionPacket(), te.worldObj.provider.dimensionId);
                    event.setCanceled(!world.isRemote);
                    FMLNetworkHandler.openGui(event.entityPlayer, PlaceableTools.instance, GuiHandler.swordSign, world, event.x, event.y, event.z);
                }
            }
        }

        if (world.isRemote) return;
        if (event.entityPlayer.isSneaking() && ToolBlock.getInstance().checkMaterial(world.getBlockMaterial(event.x, event.y, event.z), itemStack.getItem()))
        {
            int x = event.x, y = event.y, z = event.z;
            if (event.face == 1 && (itemStack.getItem() instanceof ItemSpade || itemStack.getItem() instanceof ItemHoe))
            {
                y++; // Cause the shovel gets placed above the block clicked
                if (world.isAirBlock(x, y, z) && world.isBlockSolidOnSide(event.x, event.y, event.z, ForgeDirection.UP))
                {
                    event.setCanceled(true);
                    world.setBlock(x, y, z, ToolBlock.getInstance().blockID, ToolBlock.getInstance().getMetaForLean(world, x, y, z), 3);
                    ToolBlock.getInstance().onBlockPlacedBy(world, x, y, z, event.entityPlayer, itemStack);
                }
            }
            if (event.face != 1 && event.face != 0 && (itemStack.getItem() instanceof ItemAxe || itemStack.getItem() instanceof ItemPickaxe))
            {
                ForgeDirection direction = ForgeDirection.VALID_DIRECTIONS[event.face];
                x += direction.offsetX;
                y += direction.offsetY;
                z += direction.offsetZ;

                if (world.isAirBlock(x, y, z) && world.isBlockSolidOnSide(event.x, event.y, event.z, direction.getOpposite()))
                {
                    event.setCanceled(true);
                    world.setBlock(x, y, z, ToolBlock.getInstance().blockID, direction.getOpposite().ordinal(), 3);
                    ToolBlock.getInstance().onBlockPlacedBy(world, x, y, z, event.entityPlayer, itemStack);
                }
            }
            if (itemStack.getItem() instanceof ItemSword)
            {
                ForgeDirection direction = ForgeDirection.VALID_DIRECTIONS[event.face];
                x += direction.offsetX;
                y += direction.offsetY;
                z += direction.offsetZ;

                if (world.isAirBlock(x, y, z) && world.isBlockSolidOnSide(event.x, event.y, event.z, direction.getOpposite()))
                {
                    int meta = direction.getOpposite().ordinal();
                    if (meta == 0)
                    {
                        meta = world.rand.nextInt(2);
                    }
                    event.setCanceled(true);
                    world.setBlock(x, y, z, ToolBlock.getInstance().blockID, meta, 3);
                    ToolBlock.getInstance().onBlockPlacedBy(world, x, y, z, event.entityPlayer, itemStack);
                }
            }
        }

        if (event.entityPlayer.isSneaking() && (itemStack.getItem() instanceof ItemBucket || itemStack.getItem() instanceof ItemBucketMilk))
        {
            int x = event.x, y = event.y, z = event.z;
            y++;
            if (world.isAirBlock(x, y, z) && world.isBlockSolidOnSide(event.x, event.y, event.z, ForgeDirection.UP))
            {
                event.setCanceled(true);
                world.setBlock(x, y, z, BucketBlock.getInstance().blockID, 0, 3);
                BucketBlock.getInstance().onBlockPlacedBy(world, x, y, z, event.entityPlayer, itemStack);
            }
        }
    }
}
