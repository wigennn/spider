package com.redbyte.platform.worker;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 如果互联网信息是互相连通的话，那就永远跑不完
 * </p>
 *
 * @author wangwq
 */
public class SpiderWorker {

    private static ExecutorService threadPool = Executors.newCachedThreadPool();
    private static final List<LinkedBlockingQueue> queueList = new ArrayList<>();

    private static volatile Set<String> htmlUrls = new LinkedHashSet<>();


    public static void main(String[] args) {
        String url = "http://www.baidu.com";

        try {
            start(url, htmlUrls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(htmlUrls);
    }

    private static void start(String url, Set<String> htmlUrls) throws Exception {

        int size = 300;
        if (htmlUrls.size() > size) return;

        if (StringUtils.isEmpty(url)) {
            return;
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {

            // html
            String html = response.body().string();
            System.out.println(html);

            // 预先分析出url，放入下次爬虫队列中
            preAnalyzeUrl(html, htmlUrls);

            if (!CollectionUtils.isEmpty(htmlUrls)) {
                htmlUrls.forEach(htmlUrl -> {
                    threadPool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (htmlUrls.size() > size) return;
                                start(htmlUrl, htmlUrls);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                });
            }

            // 取队列数据分析

            // 持久化数据

        }
    }

    /**
     * 分析出url
     */
    private static void preAnalyzeUrl(String html, Set<String> urls) {
        Pattern pattern = Pattern.compile("((https?|ftp|file):)?//[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            int end = matcher.end();
            String re = matcher.group();
            synchronized (htmlUrls) {
                urls.add(fixUrl(re));
            }

            System.out.println("正则结果:" + re);

            String hh = html.substring(end);
            preAnalyzeUrl(hh, urls);
        }
    }

    private static String fixUrl(String url) {
        if (url.startsWith("//")) url = "http:" + url;
        return url;
    }
}
