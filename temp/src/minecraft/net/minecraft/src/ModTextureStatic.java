package net.minecraft.src;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TextureFX;
import org.lwjgl.opengl.GL11;

public class ModTextureStatic extends TextureFX {

   private boolean oldanaglyph;
   private int[] pixels;


   public ModTextureStatic(int var1, int var2, BufferedImage var3) {
      this(var1, 1, var2, var3);
   }

   public ModTextureStatic(int var1, int var2, int var3, BufferedImage var4) {
      super(var1);
      this.pixels = null;
      this.field_76849_e = var2;
      this.field_76847_f = var3;
      this.func_76845_a(ModLoader.getMinecraftInstance().field_71446_o);
      int var5 = GL11.glGetTexLevelParameteri(3553, 0, 4096) / 16;
      int var6 = GL11.glGetTexLevelParameteri(3553, 0, 4097) / 16;
      int var7 = var4.getWidth();
      int var8 = var4.getHeight();
      this.pixels = new int[var5 * var6];
      this.field_76852_a = new byte[var5 * var6 * 4];
      if(var7 == var8 && var7 == var5) {
         var4.getRGB(0, 0, var7, var8, this.pixels, 0, var7);
      } else {
         BufferedImage var9 = new BufferedImage(var5, var6, 6);
         Graphics2D var10 = var9.createGraphics();
         var10.drawImage(var4, 0, 0, var5, var6, 0, 0, var7, var8, (ImageObserver)null);
         var9.getRGB(0, 0, var5, var6, this.pixels, 0, var5);
         var10.dispose();
      }

      this.update();
   }

   public void update() {
      for(int var1 = 0; var1 < this.pixels.length; ++var1) {
         int var2 = this.pixels[var1] >> 24 & 255;
         int var3 = this.pixels[var1] >> 16 & 255;
         int var4 = this.pixels[var1] >> 8 & 255;
         int var5 = this.pixels[var1] >> 0 & 255;
         if(this.field_76851_c) {
            int var6 = (var3 + var4 + var5) / 3;
            var5 = var6;
            var4 = var6;
            var3 = var6;
         }

         this.field_76852_a[var1 * 4 + 0] = (byte)var3;
         this.field_76852_a[var1 * 4 + 1] = (byte)var4;
         this.field_76852_a[var1 * 4 + 2] = (byte)var5;
         this.field_76852_a[var1 * 4 + 3] = (byte)var2;
      }

      this.oldanaglyph = this.field_76851_c;
   }

   public void func_76846_a() {
      if(this.oldanaglyph != this.field_76851_c) {
         this.update();
      }

   }

   public static BufferedImage scale2x(BufferedImage var0) {
      int var1 = var0.getWidth();
      int var2 = var0.getHeight();
      BufferedImage var3 = new BufferedImage(var1 * 2, var2 * 2, 2);

      for(int var4 = 0; var4 < var2; ++var4) {
         for(int var5 = 0; var5 < var1; ++var5) {
            int var6 = var0.getRGB(var5, var4);
            int var7;
            if(var4 == 0) {
               var7 = var6;
            } else {
               var7 = var0.getRGB(var5, var4 - 1);
            }

            int var8;
            if(var5 == 0) {
               var8 = var6;
            } else {
               var8 = var0.getRGB(var5 - 1, var4);
            }

            int var9;
            if(var5 >= var1 - 1) {
               var9 = var6;
            } else {
               var9 = var0.getRGB(var5 + 1, var4);
            }

            int var10;
            if(var4 >= var2 - 1) {
               var10 = var6;
            } else {
               var10 = var0.getRGB(var5, var4 + 1);
            }

            int var11;
            int var12;
            int var13;
            int var14;
            if(var7 != var10 && var8 != var9) {
               var11 = var8 != var7?var6:var8;
               var12 = var7 != var9?var6:var9;
               var13 = var8 != var10?var6:var8;
               var14 = var10 != var9?var6:var9;
            } else {
               var11 = var6;
               var12 = var6;
               var13 = var6;
               var14 = var6;
            }

            var3.setRGB(var5 * 2, var4 * 2, var11);
            var3.setRGB(var5 * 2 + 1, var4 * 2, var12);
            var3.setRGB(var5 * 2, var4 * 2 + 1, var13);
            var3.setRGB(var5 * 2 + 1, var4 * 2 + 1, var14);
         }
      }

      return var3;
   }
}
