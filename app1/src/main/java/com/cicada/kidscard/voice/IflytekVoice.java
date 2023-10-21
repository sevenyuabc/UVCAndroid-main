package com.cicada.kidscard.voice;

import android.os.Bundle;
import android.text.TextUtils;

import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.utils.LogUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 科大讯飞语音操作
 *
 * @author hwp
 */
public class IflytekVoice {

    private static IflytekVoice instance = null;
    /**
     * 语音合成对象
     */
    private SpeechSynthesizer mTTS_SpeechSynthesizer;
    /**
     * 引擎类型,云端、本地
     */
    private final String mEngineType = SpeechConstant.TYPE_LOCAL;
    /**
     * 默认发音人
     */
    private final String voicer = "xiaoyan";

    private List<String> playList;

    public static IflytekVoice getInstance() {
        if (instance == null) {
            synchronized (IflytekVoice.class) {
                instance = new IflytekVoice();
            }
        }
        return instance;
    }

    private IflytekVoice() {
        initParams();
    }


    /**
     * 初始化音频信息操作
     */
    private void initParams() {
        playList = new ArrayList<>();

        // 初始化合成对象
        mTTS_SpeechSynthesizer = SpeechSynthesizer.createSynthesizer(AppContext.getContext(), mTtsInitListener);
        // 清空参数
        mTTS_SpeechSynthesizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置合成
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            // 设置使用云端引擎
            mTTS_SpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置发音人
            mTTS_SpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,
                    voicer);
        } else {
            // 设置使用本地引擎
            mTTS_SpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置发音人资源路径
            mTTS_SpeechSynthesizer.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
            // 设置发音人 voicer为空默认通过语音+界面指定发音人。
            mTTS_SpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, voicer);
        }
        // 设置语速
        mTTS_SpeechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
        // 设置音调
        mTTS_SpeechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
        // 设置音量
        mTTS_SpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "100");
        // 设置播放器音频流类型
        mTTS_SpeechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, "3");
        Setting.setShowLog(false);
    }

    /**
     * 初始化监听。
     */
    private final InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
            } else {
            }
        }
    };

    /**
     * 播放音频
     *
     * @param text
     */
    public synchronized void playMessage(final String text) {
        try {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            int code = mTTS_SpeechSynthesizer.startSpeaking(text, new SynthesizerListener() {
                @Override
                public void onSpeakBegin() {
                }

                @Override
                public void onBufferProgress(int i, int i1, int i2, String s) {

                }

                @Override
                public void onSpeakPaused() {
                }

                @Override
                public void onSpeakResumed() {
                }

                @Override
                public void onSpeakProgress(int i, int i1, int i2) {

                }

                @Override
                public void onCompleted(SpeechError speechError) {
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {

                }
            });
            LogUtils.d(">>>语音播报>>", text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取发音人资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        // 合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(AppContext.getContext(), ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        // 发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(
                AppContext.getContext(), ResourceUtil.RESOURCE_TYPE.assets, "tts/" + voicer + ".jet"));
        return tempBuffer.toString();
    }

    /**
     * 停止语音
     */
    public void stopVoice() {
        if (mTTS_SpeechSynthesizer != null) {
            mTTS_SpeechSynthesizer.stopSpeaking();
            // 退出时释放连接
            mTTS_SpeechSynthesizer.destroy();
        }
    }

}
