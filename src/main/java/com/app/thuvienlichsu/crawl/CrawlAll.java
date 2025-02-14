package com.app.thuvienlichsu.crawl;

import com.app.thuvienlichsu.base.*;
import com.app.thuvienlichsu.crawl.crawlers.*;
import com.app.thuvienlichsu.crawl.linkers.DiTichToLeHoi;
import com.app.thuvienlichsu.crawl.linkers.DiTichToThoiKy;
import com.app.thuvienlichsu.crawl.linkers.LeHoiToDiTich;
import com.app.thuvienlichsu.crawl.linkers.NhanVatToThoiKy;
import com.app.thuvienlichsu.util.Config;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CrawlAll
{
    public void crawl()
    {
        Thread thread1 = new Thread(() -> {
            ThoiKyCrawler thoiKyCrawler = new ThoiKyCrawler();
            thoiKyCrawler.createThoiKyJson();
        });

        Thread thread2 = new Thread(() -> {
            LeHoiCrawler leHoiCrawler = new LeHoiCrawler();
            leHoiCrawler.createLeHoiJson();
        });

        Thread thread3 = new Thread(() -> {
            DiTichCrawler diaDanhCrawler = new DiTichCrawler();
            diaDanhCrawler.createDiTichJson();
        });

        Thread thread4 = new Thread(() -> {
            SuKienCrawler suKienCrawler = new SuKienCrawler();
            suKienCrawler.createSuKienJson();
        });

        Thread thread5 = new Thread(() -> {
            NhanVatCrawler nhanVatCrawler = new NhanVatCrawler();
            nhanVatCrawler.createNhanVatJson();
        });

        // Start all threads
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        // Wait for all threads to finish
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void link()
    {
        DiTichToThoiKy diTichToThoiKy = new DiTichToThoiKy();
        diTichToThoiKy.diaDanhToThoiKy();

        DiTichToLeHoi diTichToLeHoi = new DiTichToLeHoi();
        diTichToLeHoi.DiTichToLeHoi();

        LeHoiToDiTich leHoiToDiTich = new LeHoiToDiTich();
        leHoiToDiTich.LinkLeHoiToDiTich();

        NhanVatToThoiKy nhanVatToThoiKy = new NhanVatToThoiKy();
        nhanVatToThoiKy.linkNhanVatToThoiKy();
    }

    public void overwriteDatabase() {
        SCrawler sCrawler = new SCrawler() {};

        List<NhanVatModel> nhanVatModels = sCrawler.loader(Config.TEMP_NHAN_VAT_FILENAME, new TypeToken<List<NhanVatModel>>() {});
        sCrawler.writeJson(Config.NHAN_VAT_FILENAME, nhanVatModels);

        List<DiTichModel> diTichModels = sCrawler.loader(Config.TEMP_DI_TICH_FILENAME, new TypeToken<List<DiTichModel>>() {});
        sCrawler.writeJson(Config.DI_TICH_FILENAME, diTichModels);

        List<ThoiKyModel> thoiKyModels = sCrawler.loader(Config.TEMP_THOI_KY_FILENAME, new TypeToken<List<ThoiKyModel>>() {});
        sCrawler.writeJson(Config.THOI_KY_FILENAME, thoiKyModels);

        List<SuKienModel> suKienModels = sCrawler.loader(Config.TEMP_SU_KIEN_FILENAME, new TypeToken<List<SuKienModel>>() {});
        sCrawler.writeJson(Config.SU_KIEN_FILENAME, suKienModels);
        
        List<LeHoiModel> leHoiModels = sCrawler.loader(Config.TEMP_LE_HOI_FILENAME, new TypeToken<List<LeHoiModel>>() {});
        sCrawler.writeJson(Config.LE_HOI_FILENAME, leHoiModels);
    }

    // Test
    public static void main(String[] args) {
        CrawlAll crawlAll = new CrawlAll();
        crawlAll.crawl();
        crawlAll.link();
        crawlAll.overwriteDatabase();
    }
}
