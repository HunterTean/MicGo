package com.micgo.demos.nio;

/**
 * Created by liuhongtian on 17/5/8.
 */

public class RecordingDetail {

    public String songId;
    public String accompanyUrl;     // 伴奏url
    public String originalUrl;      // 原唱url

    // 是否打分 打分类型 打分url

    public boolean isAudio;
    public boolean isDuet;
    public boolean isJoin;

    public String startRecordTime;  // 开始录制时间 年月日时分秒
    public int leftStorage;         // 剩余内存

    public RecordingDetail(String songId, String accompanyUrl, String originalUrl, boolean isAudio, boolean isDuet, boolean isJoin, String startRecordTime, int leftStorage) {
        this.songId = songId;
        this.accompanyUrl = accompanyUrl;
        this.originalUrl = originalUrl;
        this.isAudio = isAudio;
        this.isDuet = isDuet;
        this.isJoin = isJoin;
        this.startRecordTime = startRecordTime;
        this.leftStorage = leftStorage;
    }

}
