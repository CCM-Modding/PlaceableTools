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

package ccm.placeableTools;

import ccm.placeableTools.block.BucketBlock;
import ccm.placeableTools.block.BucketTE;
import ccm.placeableTools.block.ToolBlock;
import ccm.placeableTools.block.ToolTE;
import ccm.placeableTools.util.EventHandler;
import ccm.placeableTools.util.GuiHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        new ToolBlock(PlaceableTools.getConfig().toolBlockID);
        GameRegistry.registerTileEntity(ToolTE.class, "ToolTE");
        GameRegistry.registerBlock(ToolBlock.getInstance(), "ToolBlock");
        LanguageRegistry.addName(ToolBlock.getInstance(), "ToolBlock");

        new BucketBlock(PlaceableTools.getConfig().bucketBlockID);
        GameRegistry.registerTileEntity(BucketTE.class, "BucketTE");
        GameRegistry.registerBlock(BucketBlock.getInstance(), "BucketBlock");
        LanguageRegistry.addName(BucketBlock.getInstance(), "BucketBlock");

        EventHandler.INSTANCE.init();
        GuiHandler.INSTANCE.init();
    }

    public void init(FMLInitializationEvent event)
    {

    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
