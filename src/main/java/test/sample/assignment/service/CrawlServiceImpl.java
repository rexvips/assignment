package test.sample.assignment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import test.sample.assignment.components.Details;
import test.sample.assignment.components.ResponseBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CrawlServiceImpl implements CrawlService {

    private static final String HTTP = "http";

    public static void main(String... args) {
        try {
            getPageLinks("http://www.google.com");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void getPageLinks(String crawlUrl) throws JsonProcessingException {
        ResponseBean rb = new ResponseBean();
        List<Details> detailsList = new ArrayList<>();
        if (!crawlUrl.isEmpty()) {
            try {
                System.out.println("crawlUrl: "+crawlUrl);
                Document document = Jsoup.connect(crawlUrl).get();
                Elements links = document.select("a[href]");
                Elements images = document.getElementsByTag("img");
                rb.setTotalLinks(links.size());
                Long count = images.stream().map(img->img.attr("abs:src")).filter(Objects::nonNull).count();
                rb.setTotalImages(count.intValue());
                for(Element link : links) {
                    String urlLink = link.attr("abs:href");
                    if(!urlLink.isEmpty()) {
                        Document internalLinksDoc = Jsoup.connect(crawlUrl).get();
                        Details d = new Details();
                            d.setPageTitle(internalLinksDoc.title());
                            d.setPageLink(urlLink);
                            d.setImage_count(internalLinksDoc.getElementsByTag("img").size());
                        detailsList.add(d);
                    }
                }
                rb.setDetails(detailsList);
                ObjectMapper om = new ObjectMapper();
                System.out.println(om.writeValueAsString(rb));
            } catch (IOException e) {
                System.err.println("For '" + crawlUrl + "': " + e.getMessage());
            }
        }
    }

    @Override
    public ResponseBean getCrawlData(String url) {
        ResponseBean responseBean = new ResponseBean();
        List<Details> detailsList = new ArrayList<>();
        if (!url.isEmpty()) {
            System.out.println("Crawling Link:" + url);
            if(!url.contains(HTTP)) url = HTTP+"://"+ url;
            try {
                Document document = Jsoup.connect(url).get();
                Elements links = document.select("a[href]");
                Elements images = document.getElementsByTag("img");
                responseBean.setTotalLinks(links.size());
                Long count = images.stream().map(img->img.attr("abs:src")).filter(Objects::nonNull).count();
                responseBean.setTotalImages(count.intValue());
                for(Element link : links) {
                    String urlLink = link.attr("abs:href");
                    if(!urlLink.isEmpty()) {
                        Document internalLinksDoc = Jsoup.connect(url).get();
                        Details d = new Details();
                        d.setPageTitle(internalLinksDoc.title());
                        d.setPageLink(urlLink);
                        d.setImage_count(internalLinksDoc.getElementsByTag("img").size());
                        detailsList.add(d);
                    }
                }
                responseBean.setDetails(detailsList);
            } catch (IOException e) {
                System.err.println("For '" + url + "': " + e.getMessage());
            }
        }
        return responseBean;
    }
}
