package net.biryeongtrain.text_emulator.utils;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class Codecs {
    public static <E> Codec<E> orCompressed(Codec<E> uncompressedCodec, Codec<E> compressedCodec) {
        return new Codec<E>() {
            @Override
            public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
                return ops.compressMaps() ? compressedCodec.encode(input, ops, prefix) : uncompressedCodec.encode(input, ops, prefix);
            }

            @Override
            public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
                return ops.compressMaps() ? compressedCodec.decode(ops, input) : uncompressedCodec.decode(ops, input);
            }

            public String toString() {
                return uncompressedCodec + " orCompressed " + compressedCodec;
            }
        };
    }

    public static <E> Codec<E> rawIdChecked(ToIntFunction<E> elementToRawId, IntFunction<E> rawIdToElement, int errorRawId) {
        return Codec.INT
                .flatXmap(
                        rawId -> (DataResult) Optional.ofNullable(rawIdToElement.apply(rawId))
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown element id: " + rawId)),
                        element -> {
                            int j = elementToRawId.applyAsInt(element);
                            return j == errorRawId ? DataResult.error(() -> "Element with unknown id: " + element) : DataResult.success(j);
                        }
                );
    }
}
