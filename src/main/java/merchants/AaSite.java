package merchants;

import main.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AaSite {
    private final String URL1 = "https://alphaart.ru/catalog/?q=";
    private final String URL2 = "&s.x=0&s.y=0";
    private final String username;
    private final String password;

    {
        try {
            username = new BufferedReader(new FileReader(Main.PATH_FOR_USERNAME)).readLine();
            password = new BufferedReader(new FileReader(Main.PATH_FOR_PASSWORD)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private final String login = username + ":" + password;
    private final String base64login = Base64.getEncoder().encodeToString(login.getBytes());

    public String getResponseText(String message) {
        var doc = getDoc(message);
        var elementParent = getArticleElement(doc, message);
        var articleName = getArticleName(elementParent, doc);
        var articleQuantity = getArticleQuantity(elementParent);
        return articleName + "\n" + "\n" + articleQuantity;
    }

    public String getPhotoLink(String message) {
        var doc = getDoc(message);
        var elementParent = getArticleElement(doc, message);
        return getArticlePhotoLink(elementParent);
    }

    private Document getDoc(String message) {
        Document doc = null;
        try {
            doc = Jsoup
                    .connect(URL1 + message + URL2)
                    .header("Authorization", "Basic " + base64login)
                    .maxBodySize(0)
                    .get();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return doc;
    }

    private String getArticleName(Element elementParent, Document document) {
        var count = countArticles(document);
        if (elementParent != null) {
            return elementParent.firstElementChild().attributes().get("title");
        }
        return count == 0 ? "Возможно, что-то напутали с артикулом, попробуйте еще раз!"
                : "Видимо, не указали цвет.";
    }

    private String getArticlePhotoLink(Element elementParent) {
        if (elementParent != null) {
            var imagePart = elementParent.firstElementChild().attributes().get("href");
            if (imagePart.contains("upload")) {
                return "https://alphaart.ru" + elementParent.firstElementChild().attributes().get("href");
            } else {
                return "AgACAgIAAxkBAAIvGWeRaC2A4Fh7irKs7r_ijbdnTxzyAALC6TEbIm2RSPvxJUJrMlOGAQADAgADcwADNgQ";
            }
        }
        return "";
    }

    private String getArticleQuantity(Element elementParent) {
        if (elementParent != null) {
            var quantity =  elementParent.getElementsByAttributeValue("class", "bx_catalog_item_articul")
                    .stream()
                    .map(Element::text)
                    .filter(data -> data.contains("Наличие: "))
                    .collect(Collectors.joining());
            if (quantity.contains(">")) {
                quantity = quantity.replace(" >", "> ");
            }
            return quantity;
        }
        return "";
    }

    private Element getArticleElement(Document document, String message) {
        if (document != null) {
            return document.getElementsByAttributeValue("class", "bx_catalog_item_container")
                    .stream()
                    .filter(el -> extractArticleName(el).equals(message))
                    .findFirst().orElse(null);
        }
        return null;
    }

    private String extractArticleName(Element elementParent) {
        return elementParent.stream()
                .map(Element::children)
                .map(el -> {
                    Elements essentialEls = new Elements();
                    for (var e : el) {
                        if (e.hasClass("bx_catalog_item_articul")) {
                            essentialEls.add(e);
                        }
                    }
                    return essentialEls;
                })
                .map(Elements::text)
                .filter(t -> t.contains("Артикул:"))
                .map(a -> a.split(" ")[1])
                .collect(Collectors.joining());
    }

    private long countArticles(Document document) {
        if (document == null) {
            return 0;
        }
        return document.getElementsByAttributeValue("class", "bx_catalog_item_container").stream().count();
    }
}
