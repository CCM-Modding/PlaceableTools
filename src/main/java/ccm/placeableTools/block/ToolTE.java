/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014. Dries K. Aka Dries007 and the CCM modding crew.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ccm.placeableTools.block;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import static ccm.placeableTools.util.PTConstants.TEXT_JOINER;

public class ToolTE extends TileEntity
{
    private ItemStack stack;

    public int      sign1Facing = -1;
    public String[] sign1Text   = {"", "", "", ""};

    public int      sign2Facing = -1;
    public String[] sign2Text   = {"", "", "", ""};
    public int      lastAdded   = 0;

    public ToolTE()
    {

    }

    public ToolTE(World world)
    {
        setWorldObj(world);
    }

    public void placeBlock(ItemStack stack)
    {
        if (stack != null) this.stack = stack.copy();
    }

    public void removeBlock(World world)
    {
        if (world.isRemote) return;
        dropItem(stack);
        if (sign1Facing != -1) dropItem(new ItemStack(Item.sign));
        if (sign2Facing != -1) dropItem(new ItemStack(Item.sign));
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
        sign1Facing = tag.getInteger("sign1Facing");
        sign1Text = tag.getString("sign1Text").split("\n");
        sign2Facing = tag.getInteger("sign2Facing");
        sign2Text = tag.getString("sign2Text").split("\n");
    }

    public void writeToNBT(NBTTagCompound tag)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
        super.writeToNBT(tag);
        tag.setCompoundTag("stack", getStack().writeToNBT(new NBTTagCompound()));
        tag.setInteger("sign1Facing", sign1Facing);
        tag.setString("sign1Text", TEXT_JOINER.join(sign1Text));
        tag.setInteger("sign2Facing", sign2Facing);
        tag.setString("sign2Text", TEXT_JOINER.join(sign2Text));
    }

    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        stack = ItemStack.loadItemStackFromNBT(pkt.data.getCompoundTag("stack"));
        sign1Facing = pkt.data.getInteger("sign1Facing");
        sign1Text = pkt.data.getString("sign1Text").split("\n");
        sign2Facing = pkt.data.getInteger("sign2Facing");
        sign2Text = pkt.data.getString("sign2Text").split("\n");
        lastAdded = pkt.data.getInteger("lastAdded");
    }

    public Packet getDescriptionPacket()
    {
        NBTTagCompound root = new NBTTagCompound();
        root.setCompoundTag("stack", getStack().writeToNBT(new NBTTagCompound()));
        root.setInteger("sign1Facing", sign1Facing);
        root.setString("sign1Text", TEXT_JOINER.join(sign1Text));
        root.setInteger("sign2Facing", sign2Facing);
        root.setString("sign2Text", TEXT_JOINER.join(sign2Text));
        root.setInteger("lastAdded", lastAdded);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 15, root);
    }

    public int addSign(int coordBaseMode)
    {
        if (sign1Facing == -1)
        {
            sign1Facing = coordBaseMode;
            lastAdded = 1;
            return 1;
        }
        if (sign2Facing == -1)
        {
            sign2Facing = coordBaseMode;
            lastAdded = 2;
            return 2;
        }
        return -1;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.getAABBPool().getAABB(xCoord - 1, yCoord - 1, zCoord - 1, xCoord + 2, yCoord + 2, zCoord + 2);
    }

    public void setText(boolean b, String[] strings)
    {
        if (b)
        {
            lastAdded = 1;
            sign1Text = strings;
        }
        else
        {
            lastAdded = 2;
            sign2Text = strings;
        }
        PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
    }


    public void onNeighborBlockChange()
    {
        int meta = getBlockMetadata();
        ForgeDirection direction = ForgeDirection.VALID_DIRECTIONS[meta];

        if (stack.getItem() instanceof ItemSpade || stack.getItem() instanceof ItemHoe) direction = ForgeDirection.DOWN;

        Material material = worldObj.getBlockMaterial(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);

        if (material == null || stack == null || !ToolBlock.getInstance().checkMaterial(material, stack.getItem())) //We need to pop off
        {
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
    }
}