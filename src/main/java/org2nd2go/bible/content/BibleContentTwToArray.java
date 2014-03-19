package org2nd2go.bible.content;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;
import org2nd2go.bible.basic.*;
//import org.jsoup.select.;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * http://bible.fhl.net/new/fdl.txt
 *
 * @author mark
 */
public class BibleContentTwToArray implements Title, TitleShort {

    String CBOL_HOME = "/home/mark/bible/tw/";

    public static void main(String[] args) throws IOException, ParseException {

        BibleContentTwToArray bc = new BibleContentTwToArray();
//        bc.makeTextbook();
        String str = bc.getBookData(1, 9);// result of 66 is too big. more than 3BM, NetBeans complains
        StringBuilder sb = new StringBuilder();

        sb.append("package org2nd2go.bible.tw;");
        sb.append("\npublic interface BibleData {");
        sb.append("\nString[][][]BOOK={");
        sb.append("\n{\n{\"Chinese Bible, data source from ...\"}},");

        sb.append(str);

        sb.deleteCharAt(sb.length() - 1);
        sb.append("};\n");
        sb.append("}");

//        bc.createSQL(str);
//        System.out.println(sb.toString());
        bc.createFile("BOOK_ARRAY.txt", sb.toString());
        System.out.println(" ... please check /home/mark/go/bible_tw_api/src/main/java/org2nd2go/bible/tw/BibleData.java");

    }
//        new BibleContentTw().getContent();
//        new BibleContentTw().getContent("tw",1,1);
//        new BibleContentTw().getContent("tw",40,6);

    public void makeTextbook() throws ParseException, IOException {

        String note1 = "\nsource from:"
                + "\n  http://a2z.fhl.net/CBOL.html"
                + "\n  本畫面由信望愛資訊中心之CBOL計畫產生，歡迎連結，無須申請。"
                + "\n  CBOL計畫之資料版權宣告採用GNU Free Documentation License。"
                + "\n  願上帝的話能建造每一位使用這系統的人，來榮耀祂自己的名";
        String note2 = "\n\nconverted by:"
                + "\n  mark@2ng2go.org"
                + "\n  3/18/2014 ";
        String textbook = getBookContent() + note1 + note2;

        createBook(textbook);

//        System.out.println(textbook);
//        bc.getBookContent("tw");
    }

    public void createFile(String filename, String data) {
        //String filename = "bible_chinese.txt";
//Path file = Paths.get(CBOL_HOME + filename);

        String PROJECT_filename = "/home/mark/go/bible_tw_api/src/main/java/org2nd2go/bible/tw/BibleData.java";
        Path file = Paths.get(PROJECT_filename);

        Charset charset = Charset.forName("UTF-8");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            writer.write(data, 0, data.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

    }

    public void createBook(String s) {
        String filename = "bible_chinese.txt";

        Path file = Paths.get(CBOL_HOME + filename);

        Charset charset = Charset.forName("UTF-8");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            writer.write(s, 0, s.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

    }

    public void createSQL(String s) {
        String filename = "bible_chinese.sql";

        Path file = Paths.get(CBOL_HOME + filename);

        Charset charset = Charset.forName("UTF-8");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            writer.write(s, 0, s.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

    }

    public String getBookContent() throws ParseException, IOException {
        StringBuilder sb = new StringBuilder();
        int book;
        int counter = 0;
        String filename;
//        for (int k = 1; k <= 66; k++) {
        for (int k = 1; k <= 66; k++) {
            book = k - 1;
            sb.append("\n\n Book#")
                    .append(k)
                    .append(" ")
                    .append(TITLE_TW[book])
                    .append(" ")
                    .append(TITLE_EN[book])
                    .append("\n\n");

            for (int chapter = 1; chapter <= CHAPTER_COUNT[book]; chapter++) {
                String str = getVerses(book, chapter);
//          sb.       
//                System.out.println(str);
                sb.append(str);
            }
        }
        return sb.toString();
    }

//    
//    public String getBookData() throws ParseException, IOException {
//    
//    }
    public String getBookData(int bookFrom, int bookTo) throws ParseException, IOException {

        StringBuilder sb = new StringBuilder();

//        sb.append("\n{ \"book " + bookFrom + " to " + bookTo + "\"},");
//        for (int k = 1; k <= 1; k++) {
        for (int k = bookFrom; k <= bookTo; k++) {
            sb.append("\n{");
            sb.append("\n{ \"book#" + k + "\"},");

            for (int chapter = 1; chapter <= CHAPTER_COUNT_1TO66[k]; chapter++) {
                String str = getChapterData(k, chapter);
                sb.append(str);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
//            sb.append("},");
            sb.append("},");

        }
//        sb.deleteCharAt(sb.length() - 1);
//        sb.append("},");

        return sb.toString();
    }

    public String getBookSource(int book, int chapter) {
        StringBuilder sb = new StringBuilder();
        String filename = "unv_" + TITLE_SHORT_EN[book] + "_" + chapter + ".html";

        Path file = Paths.get(CBOL_HOME + filename);

        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
//                System.out.println(line);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        return sb.toString();
    }

    public String getBookSource1to66(int book, int chapter) {
        StringBuilder sb = new StringBuilder();
        String filename = "unv_" + TITLE_SHORT_EN_1TO66[book] + "_" + chapter + ".html";

        Path file = Paths.get(CBOL_HOME + filename);

        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
//                System.out.println(line);
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        return sb.toString();
    }

    /**
     *
     * @param ver tw for traditional Chinese, kjv for
     * @param book 1 to 66
     */
//    public void createBookSql(String ver) throws ParseException, IOException {
//        StringBuilder sb = new StringBuilder();
//        sb.append("-- \n");
//        sb.append("-- generated by 2nd2go.org\n");
//        sb.append("-- \n\n");
////        Charset charset = Charset.forName("US-ASCII");
//        Charset charset = Charset.forName("UTF-8");
////      
////        if (ver.equals("tw")) {
////            charset = Charset.forName("UTF-8");
////        }
//        //    TODO
////        Path file = Paths.get(BIBLE_BASE + "Matt" + ".verseData");
//        Path file = Paths.get(CBOL_HOME + "Matt" + ".verseData");
//
//        for (int book = 40; book <= 40; book++) {
//
//            int k = book - 1;
//            int chapCnt = CHAPTER_COUNT[k];
//            System.out.println("-- book#" + book + ", " + TITLE_EN[k] + ", " + TITLE_TW[k] + ", chapters=" + chapCnt);
//            sb.append("\n-- book#" + book + ", " + TITLE_EN[k] + ", " + TITLE_TW[k] + ", chapters=" + chapCnt);
////
////        
////        
//
////        
////        
////        
//            for (int chap = 1; chap <= chapCnt; chap++) {
////            System.out.println(getInsertStatement(ver, book, chap));
////                String s = getInsertStatement(ver, book, chap);
//                String s = "TODO";
//
//                System.out.println("  ... debug ..." + s);
//                sb.append(s);
//
//            }
//        }
//
//        //
//        //
//        //
//        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
//            writer.write(sb.toString(), 0, sb.toString().length());
//        } catch (IOException x) {
//            x.printStackTrace();
//            System.err.format("%n%n createBookSql   ver=%s %n%n", ver);
//        }
//
//    }
    public String getFilename(int book, int chapter) {

        StringBuilder sb = new StringBuilder();

        return sb.toString();

    }

    public String getOneChapterHtmlSource(String ver, String book, int chapter) throws ParseException, IOException {
        Path file = Paths.get("/home/mark/bible/cobl/unv_" + book + "_" + chapter + ".html");
        StringBuilder sb = new StringBuilder();

        Charset charset = Charset.forName("UTF-8");

        String line = null;

        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            while ((line = reader.readLine()) != null) {

                sb.append(line);
//                System.out.println(line);
            }
        } catch (IOException x) {
            System.err.format("getOneChapterContent  ...IOException:", x);
            System.err.format("%nver=%s", ver);
//            System.err.format("%nbook chap=%s%n", bookChapter);
            System.err.format("%n%s%n", sb.toString());

//            System.exit(-1);
        }
        return sb.toString();
    }

    public String getBookChapter(int book, int chapter) {
        String strBook = String.format("B%02d", book);
        String strChapter = String.format("C%03d", chapter);
        return strBook + strChapter;
    }

    public String getInsertStatement(String book, int chapter) throws ParseException, IOException {
//        String strBook = String.format("B%02d", book);
//        String strChapter = String.format("C%03d", chapter);
//        String strHtml = getOneChapterHtmlSource(ver, getBook/home/mark/bible/checking/bgpda/hb_1_0Chapter("Matt", 6));
        String strHtml = "TODO";

        int verse = 0;
        Document doc = Jsoup.parse(strHtml);
        Elements testing = doc.select("P");

        StringBuilder sb = new StringBuilder();
        sb.append("\nINSERT INTO `bible`.`bible` (`VERSION`, `BOOK`, `CHAPTER`, `VERSE`, `CONTENT`) VALUES");
        for (Element src : testing) {
            //
            // filter out
            //
            if (src.toString().contains("<a href=")) {
                continue;
            }
            if (src.toString().length() <= 7) {
                continue;
            }
            if (src.toString().contains("<img src")) {
                continue;
            }
            if (src.toString().contains("<p>&nbsp;</p>")) {
                continue;
            }

            //
            // replace 
            //
            String plain;
            plain = src.toString().replace("<p>", "");
            plain = plain.replace("</p>", "");
            plain = plain.replace("<i>", "");
            plain = plain.replace("</i>", "");

            //
            // compose SQL
            //
            verse++;

//            System.out.println("ver="+ver+" book"+book+" chapter="+chapter+" "+verse + "=> " + data);
            String temp = String.format("%n('tw','%s','%s','%s','%s'),", book, chapter, verse, plain);
            sb.append(temp);
//            System.out.println(temp);

        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(";");

//        System.out.println(sb.toString());
        return sb.toString();

    }

    public void getVerses(String html) throws ParseException, IOException {
//        String ver = "tw";
//        String strHtml = getOneChapterHtmlSource("kjv", "B40C021");
//        String strBook = String.format("B%02d", book);
//        String strChapter = String.format("C%03d", chapter);

//        System.out.println(" ===========> "+strBook+strChapter);
//        String html = getOneChapterHtmlSource(ver, strBook , chapter);
//        System.out.println(" =========== ");
//        System.out.println(strHtml);
//        System.out.println(" =========== ");
        int chapNum = 0;
        Document doc = Jsoup.parse(html);
        Elements testing = doc.select("TD");
        for (Element src : testing) {
            if (src.toString().contains("<a href=")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            if (src.toString().length() <= 7) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            //<img src
            if (src.toString().contains("<img src")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            if (src.toString().contains("<p>&nbsp;</p>")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            if (src.toString().contains("align")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }

            chapNum++;
            String plain;
            plain = src.toString().replace("<td>", "");
            plain = plain.replace("</td>", "");
//            data = data.replace("<i>", "");
//            data = data.replace("</i>", "");
            System.out.println(chapNum + " : " + plain);

        }

    }

    public String getVerses(int book, int chapter) throws ParseException, IOException {
        String html = getBookSource(book, chapter);

        StringBuilder sb = new StringBuilder();
        int verse = 0;
        String result = "";
        Document doc = Jsoup.parse(html);
        Elements testing = doc.select("TD");
        for (Element src : testing) {
            if (src.toString().contains("<a href=")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            if (src.toString().length() <= 7) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            //<img src
            if (src.toString().contains("<img src")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            if (src.toString().contains("<p>&nbsp;</p>")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            if (src.toString().contains("align")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }

            verse++;
            String plain;
            plain = src.toString().replace("<td>", "");
            plain = plain.replace("</td>", "");

            System.out.print("(" + (1 + book) + ")" + TITLE_SHORT_TW[book] + " " + chapter + ":");
            System.out.println(verse + " " + plain);

            result += "(" + (1 + book) + ")" + TITLE_SHORT_TW[book] + " " + chapter + ":" + verse + " " + plain + "\n";
        }
        return result;

    }

    public String getChapterData(int book, int chapter) throws ParseException, IOException {
        String html = getBookSource1to66(book, chapter);
        StringBuilder sb = new StringBuilder();
        sb.append("\n{");
        sb.append("\n \"chapter " + chapter + "\",");

//        StringBuilder sb = new StringBuilder();
        int verse = 0;
        String verseData = "";

        //
        // Jsoup section
        //
        Document doc = Jsoup.parse(html);
        Elements testing = doc.select("TD");
        for (Element src : testing) {
            if (src.toString().contains("<a href=")) {
                continue;
            }
            if (src.toString().length() <= 7) {
                continue;
            }
            if (src.toString().contains("<img src")) {
                continue;
            }
            if (src.toString().contains("<p>&nbsp;</p>")) {
                continue;
            }
            if (src.toString().contains("align")) {
                continue;
            }

            verse++;
            String data;
            data = src.toString().replace("<td>", "");
            data = data.replace("</td>", "");
            verseData = String.format("\n\"%s\",", data);
            sb.append(verseData);
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");

        return sb.toString();

    }

    public void getContent(String ver, int book, int chapter) throws ParseException, IOException {
//        String ver = "tw";
//        String strHtml = getOneChapterHtmlSource("kjv", "B40C021");
        String strBook = String.format("B%02d", book);
        String strChapter = String.format("C%03d", chapter);

//        System.out.println(" ===========> "+strBook+strChapter);
        String html = getOneChapterHtmlSource(ver, strBook, chapter);

//        System.out.println(" =========== ");
//        System.out.println(strHtml);
//        System.out.println(" =========== ");
        int chapNum = 0;
        Document doc = Jsoup.parse(html);
        Elements testing = doc.select("P");
        for (Element src : testing) {
            if (src.toString().contains("<a href=")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            if (src.toString().length() <= 7) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            //<img src
            if (src.toString().contains("<img src")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            if (src.toString().contains("<p>&nbsp;</p>")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }

            chapNum++;
            String plain;
            plain = src.toString().replace("<p>", "");
            plain = plain.replace("</p>", "");
            plain = plain.replace("<i>", "");
            plain = plain.replace("</i>", "");
            if (ver.equals("tw")) {
                plain = plain.replace(" ", "");
                plain = plain.replace("．", "。");
                plain = plain.replace("、", "，");
            }
            System.out.println(chapNum + "=> " + plain);

        }

    }

    public void getContent() throws ParseException, IOException {
        String ver = "tw";
//        String strHtml = getOneChapterHtmlSource("kjv", "B40C021");
//        String html = getOneChapterContent(ver, "B40C006");
        String html = "TODO";

        System.out.println(" =========== ");
        System.out.println(html);
        System.out.println(" =========== ");
        int chapNum = 0;
        Document doc = Jsoup.parse(html);
        Elements testing = doc.select("P");
        for (Element src : testing) {
            if (src.toString().contains("<a href=")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            if (src.toString().length() <= 7) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            //<img src
            if (src.toString().contains("<img src")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }
            if (src.toString().contains("<p>&nbsp;</p>")) {
//                System.out.println("   ... to ignore");
//                System.out.println("   ... " + src);
                continue;
            }

            chapNum++;
            String plain;
            plain = src.toString().replace("<p>", "");
            plain = plain.replace("</p>", "");
            plain = plain.replace("<i>", "");
            plain = plain.replace("</i>", "");
            if (ver.equals("tw")) {
                plain = plain.replace(" ", "");
                plain = plain.replace("．", "。");
                plain = plain.replace("、", "，");
            }
            System.out.println(chapNum + "=> " + plain);

        }

    }

    public void insertDates() throws ParseException {

        // http://docs.oracle.com/javase/tutorial/essential/io/file.strHtml
//        http://docs.oracle.com/javase/tutorial/java/data/manipstrings.strHtml
        //
        Path file = Paths.get("/home/mark/0-prj/bible/bookchap-v3.csv");
        Charset charset = Charset.forName("US-ASCII");

        //
        // http://stackoverflow.com/questions/428918/how-can-i-increment-a-date-by-one-day-in-java
        //
        String dt = "2013-03-08";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dt));
        String WEEKDAY_CHINESE = "日一二三四五六";
        int id = 0;
        for (int ww = 1; ww <= 52; ww++) {
            for (int dd = 0; dd < 7; dd++) {
                id++;
                c.add(Calendar.DATE, 1);  // number of days to add
                dt = sdf.format(c.getTime());  // dt is now the new date

                System.out.printf("INSERT INTO `bible`.`dates` ( `ID` , `DATE` , `WEEKNUM` ,`WEEKDAY` ) VALUES (");
                System.out.printf("'%s', ", id);
                System.out.printf("'%s', ", dt);
                System.out.printf("'%s', ", ww);
                System.out.printf("'%s' ", WEEKDAY_CHINESE.charAt(dd));
                System.out.printf("); %n");

            }
        }
    }
}
