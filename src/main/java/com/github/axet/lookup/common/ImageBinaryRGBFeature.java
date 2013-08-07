package com.github.axet.lookup.common;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class ImageBinaryRGBFeature implements ImageBinaryFeature {

    public RGBImage image;
    public ImageBinaryChannelFeature r;
    public ImageBinaryChannelFeature g;
    public ImageBinaryChannelFeature b;

    List<ImageBinaryChannelFeature> list;

    public ImageBinaryRGBFeature(BufferedImage img, double threshold) {
        init(img);

        FeatureSet lr = new FeatureSetAuto(r, threshold);
        r.init(lr);

        FeatureSet lg = new FeatureSetAuto(r, threshold);
        g.init(lg);

        FeatureSet lb = new FeatureSetAuto(r, threshold);
        b.init(lb);
    }

    public void init(BufferedImage img) {
        image = new RGBImage();
        r = new ImageBinaryChannelFeature();
        r.integral = new IntegralImage();
        r.integral2 = new IntegralImage2();
        g = new ImageBinaryChannelFeature();
        g.integral = new IntegralImage();
        g.integral2 = new IntegralImage2();
        b = new ImageBinaryChannelFeature();
        b.integral = new IntegralImage();
        b.integral2 = new IntegralImage2();

        list = Arrays.asList(new ImageBinaryChannelFeature[] { r, g, b });

        this.image.init(img);
        this.r.initBase(this.image.r);
        this.g.initBase(this.image.g);
        this.b.initBase(this.image.b);

        for (int x = 0; x < this.image.cx; x++) {
            for (int y = 0; y < this.image.cy; y++) {
                this.image.step(x, y);
                this.r.integral.step(x, y);
                this.r.integral2.step(x, y);
                this.g.integral.step(x, y);
                this.g.integral2.step(x, y);
                this.b.integral.step(x, y);
                this.b.integral2.step(x, y);
            }
        }

        r.zeroMean = new ImageZeroMean();
        g.zeroMean = new ImageZeroMean();
        b.zeroMean = new ImageZeroMean();
        r.zeroMean.init(r.integral);
        g.zeroMean.init(g.integral);
        b.zeroMean.init(b.integral);

        for (int x = 0; x < this.image.cx; x++) {
            for (int y = 0; y < this.image.cy; y++) {
                r.zeroMean.step(x, y);
                g.zeroMean.step(x, y);
                b.zeroMean.step(x, y);
            }
        }

        r.zeroMeanIntegral = new IntegralImage();
        g.zeroMeanIntegral = new IntegralImage();
        b.zeroMeanIntegral = new IntegralImage();
        r.zeroMeanIntegral.initBase(r.zeroMean);
        g.zeroMeanIntegral.initBase(g.zeroMean);
        b.zeroMeanIntegral.initBase(b.zeroMean);

        for (int x = 0; x < this.image.cx; x++) {
            for (int y = 0; y < this.image.cy; y++) {
                r.zeroMeanIntegral.step(x, y);
                g.zeroMeanIntegral.step(x, y);
                b.zeroMeanIntegral.step(x, y);
            }
        }
    }

    public int getWidth() {
        return image.cx;
    }

    public int getHeight() {
        return image.cy;
    }

    public int size() {
        return image.cx * image.cy;
    }

    public BufferedImage getImage() {
        return image.buf;
    }

    @Override
    public List<ImageBinaryChannelFeature> getFeatureChannels() {
        return list;
    }

}
