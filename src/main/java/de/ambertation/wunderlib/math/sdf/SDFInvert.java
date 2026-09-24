package de.ambertation.wunderlib.math.sdf;

import de.ambertation.wunderlib.math.Bounds;
import de.ambertation.wunderlib.math.Float3;
import de.ambertation.wunderlib.math.Transform;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.MapCodec;

public class SDFInvert extends SDFOperation {
    public static final MapCodec<SDFInvert> DIRECT_CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Transform.CODEC.fieldOf("transform").orElse(Transform.IDENTITY).forGetter(o -> o.transform),
                    SDF.CODEC.fieldOf("sdf").forGetter(b -> b.getFirst())
            )
            .apply(instance, SDFInvert::new)
    );

    public static final MapCodec<SDFInvert> CODEC = DIRECT_CODEC;

    @Override
    public MapCodec<? extends SDF> codec() {
        return CODEC;
    }


    //-------------------------------------------------------------------------------
    protected SDFInvert(Transform t, SDF sdf) {
        super(t, sdf);
    }

    protected SDFInvert(SDF sdf) {
        this(Transform.IDENTITY, sdf);
    }

    @Override
    public double dist(Float3 pos) {
        return -getFirst().dist(pos);
    }

    @Override
    public void dist(EvaluationData d, Float3 pos) {
        getFirst().dist(d, pos);
        d.dist *= -1;
    }

    @Override
    public String toString() {
        return "!" + getFirst() + " [" + graphIndex + "]";
    }

    @Override
    public Bounds getBoundingBox() {
        return getFirst().getBoundingBox();
    }
}
