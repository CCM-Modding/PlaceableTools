package ccm.placeableTools.client;

import ccm.placeableTools.block.BucketTE;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BucketRenderer extends TileEntitySpecialRenderer
{
    private static final String MODEL = "/assets/placeabletools/models/bucket_big.obj";
    private static final IModelCustom modelBucket = AdvancedModelLoader.loadModel(MODEL);
    private static final ResourceLocation texture = new ResourceLocation("textures/blocks/iron_block.png");
    private static final ResourceLocation WATER = new ResourceLocation("textures/blocks/water_still.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f)
    {
        BucketTE bucketTE = (BucketTE) te;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y, (float) z + 1F);
        GL11.glScalef(0.0625F, 0.0625F, 0.0625F);

        bindTexture(texture);
        modelBucket.renderAllExcept("Liquid_001");

        FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(bucketTE.getStack());
        if (fluidStack != null)
        {
            Icon icon = fluidStack.getFluid().getIcon();
            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            bindTexture(TextureMap.locationBlocksTexture);
            int color = fluidStack.getFluid().getColor();
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;
            GL11.glColor4f(red, green, blue, 1.0F);

            int i = TextureUtil.glGenTextures();
            TextureMap textureMap = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationItemsTexture));
            TextureAtlasSprite textureAtlasSprite = textureMap.getAtlasSprite(icon.getIconName());
            if (textureAtlasSprite.getFrameCount() != 0) TextureUtil.uploadTexture(i, textureAtlasSprite.getFrameTextureData(0), textureAtlasSprite.getIconHeight(), textureAtlasSprite.getIconWidth());

            modelBucket.renderOnly("Liquid_001");
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
    }

    public void drawTexturedModelRectFromIcon(Icon par3Icon)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.addVertexWithUV(1,0.94500,-0, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());
        tessellator.addVertexWithUV(1,0.94500,-0, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());
        tessellator.addVertexWithUV(0,0.94500,-1, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());
        tessellator.addVertexWithUV(0,0.94500,-0, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());

        tessellator.draw();
    }
}
