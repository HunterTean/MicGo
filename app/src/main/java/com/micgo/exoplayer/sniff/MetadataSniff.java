package com.micgo.exoplayer.sniff;

import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer2.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuhongtian on 17/5/16.
 */
// 用于检验音频文件是否被底层所支持，排除异常伴奏文件
public class MetadataSniff {

    private final List<Extractor> extractors;
    private Uri uri;
    private OnCheckListener onCheckListener;

    public MetadataSniff(Uri uri, OnCheckListener onCheckListener) {
        extractors = new ArrayList<Extractor>();
        extractors.clear();
        extractors.add(new Mp3Extractor());
        extractors.add(new AdtsExtractor());
//        extractors.add(new Mp4Extractor());

        this.uri = uri;
        this.onCheckListener = onCheckListener;
    }

    public void start() {
        boolean result = false;
        try {
            long position = 0; //positionHolder.position;
            DataSource dataSource = new FileDataSource(null);
            long length = dataSource.open(
                    new DataSpec(uri, position, C.LENGTH_UNSET, Util.sha1(uri.toString())));
            if (length != C.LENGTH_UNSET) {
                length += position;
            }
            ExtractorInput input = new DefaultExtractorInput(dataSource, position, length);
            for (Extractor extractor : extractors) {
                if (extractor.sniff(input)) {
                    result = true;
                    break;
                }
            }
        } catch (EOFException e) {
            // Do nothing.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (onCheckListener != null) {
                onCheckListener.onComplete(result);
            }
        }
    }

    public interface OnCheckListener {
        public void onComplete(boolean result);
    }

}
