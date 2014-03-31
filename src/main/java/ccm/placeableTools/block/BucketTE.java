package ccm.placeableTools.block;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BucketTE extends TileEntity
{
    private ItemStack stack;

    public BucketTE()
    {

    }

    public BucketTE(World world)
    {
        setWorldObj(world);
    }

    public void placeBlock(ItemStack stack)
    {
        if (stack != null) this.stack = stack.copy();
        if (this.stack != null) this.stack.stackSize = 1;
    }

    public void removeBlock(World world)
    {
        if (world.isRemote) return;
        dropItem(stack);
    }

    public void dropItem(ItemStack stack)
    {
        if (!worldObj.isRemote && worldObj.getGameRules().getGameRuleBooleanValue("doTileDrops") && stack != null)
        {
            float f = 0.7F;
            double d0 = (double) (worldObj.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d1 = (double) (worldObj.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d2 = (double) (worldObj.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(worldObj, (double) xCoord + d0, (double) yCoord + d1, (double) zCoord + d2, stack);
            entityitem.delayBeforeCanPickup = 10;
            worldObj.spawnEntityInWorld(entityitem);
        }
    }

    public ItemStack getStack()
    {
        return stack;
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
        super.readFromNBT(tag);

        stack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("stack"));
    }

    public void writeToNBT(NBTTagCompound tag)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
        super.writeToNBT(tag);
        tag.setCompoundTag("stack", getStack().writeToNBT(new NBTTagCompound()));
    }

    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        stack = ItemStack.loadItemStackFromNBT(pkt.data.getCompoundTag("stack"));
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound root = new NBTTagCompound();
        root.setCompoundTag("stack", getStack().writeToNBT(new NBTTagCompound()));
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 15, root);
    }
}
