package ccm.placeableTools.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BucketBlock extends BlockContainer
{
    private static BucketBlock instance;

    public static BucketBlock getInstance()
    {
        return instance;
    }

    public BucketBlock(int blockID)
    {
        super(blockID, Material.circuits);
        setHardness(1.5F);
        setResistance(5F);
        setUnlocalizedName("BucketBlock");
        instance = this;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        return ((BucketTE) world.getBlockTileEntity(x, y, z)).activate(player, side, hitX, hitY, hitZ);
    }

    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        ((BucketTE) world.getBlockTileEntity(x, y, z)).standIn(entity);
    }

    @Override
    public void breakBlock(World par1World, int x, int y, int z, int blockID, int meta)
    {
        TileEntity te = par1World.getBlockTileEntity(x, y, z);
        if (te != null && te instanceof BucketTE)
        {
            BucketTE inv = (BucketTE) te;
            inv.removeBlock(par1World);
        }
        super.breakBlock(par1World, x, y, z, blockID, meta);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof BucketTE)
        {
            BucketTE inv = (BucketTE) te;
            inv.placeBlock(stack);
            if (entityLiving instanceof EntityPlayer) stack.stackSize--;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new BucketTE(world);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return false;
    }

    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return ((BucketTE) world.getBlockTileEntity(x, y, z)).getStack();
    }

    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        return new ArrayList<ItemStack>();
    }


    @SideOnly(Side.CLIENT)
    public boolean addBlockDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean addBlockHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
        return true;
    }

    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        float f = 0.03F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        this.setBlockBoundsForItemRender();
    }

    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        return ((BucketTE)world.getBlockTileEntity(x, y, z)).getLightLevel();
    }
}
