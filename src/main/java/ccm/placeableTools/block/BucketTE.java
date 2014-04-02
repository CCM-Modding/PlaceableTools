package ccm.placeableTools.block;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BucketTE extends TileEntity
{
    private ItemStack stack;
    private Fluid fluid;

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

        FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(this.stack);
        fluid = fluidStack == null ? null : fluidStack.getFluid();
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

    public Fluid getFluid()
    {
        return fluid;
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
        super.readFromNBT(tag);

        stack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("stack"));
        FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(this.stack);
        fluid = fluidStack == null ? null : fluidStack.getFluid();
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
        FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(this.stack);
        fluid = fluidStack == null ? null : fluidStack.getFluid();
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound root = new NBTTagCompound();
        root.setCompoundTag("stack", getStack().writeToNBT(new NBTTagCompound()));
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 15, root);
    }

    public int getLightLevel()
    {
        return fluid == null ? 0 : fluid.getLuminosity();
    }

    public void standIn(Entity entity)
    {
        if (worldObj.isRemote) return;
        if (fluid != null)
        {
            if (fluid.getTemperature() >= 700)
            {
                if (!entity.isImmuneToFire() && (!entity.isBurning() || worldObj.rand.nextInt(25) == 2))
                {
                    entity.attackEntityFrom(DamageSource.lava, 4.0F);
                    entity.setFire(15);
                }
            }

            if (fluid == FluidRegistry.WATER)
            {
                entity.extinguish();
            }
        }
    }

    public boolean activate(EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem();
        if (itemstack != null && fluid == FluidRegistry.WATER)
        {
            if (itemstack.getItem() instanceof ItemArmor && ((ItemArmor)itemstack.getItem()).getArmorMaterial() == EnumArmorMaterial.CLOTH)
            {
                ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                itemarmor.removeColor(itemstack);
                return true;
            }

            if (itemstack.getItem() instanceof ItemBlock)
            {
                Block block = Block.blocksList[((ItemBlock) itemstack.getItem()).getBlockID()];
                if (block instanceof BlockColored)
                {
                    itemstack.setItemDamage(0);

                    if (block == Block.stainedClay) itemstack.itemID = Block.hardenedClay.blockID; // Magic!

                    return true;
                }
            }
        }

        return false;
    }
}
