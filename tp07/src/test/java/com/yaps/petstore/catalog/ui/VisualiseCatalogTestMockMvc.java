package com.yaps.petstore.catalog.ui;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This class tests the HTML Pages and servlets.
 * 
 * Full-stack integration test with browser simultation.
 */

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // active application-test.properties en PLUS de application.properties
@Sql(
// Note : cette annotation devra être déplacée vers les méthodes
// de tests si celles-ci ont besoin de bases différentes
// (par exemple qu'on teste sur des données vides)
"/testsql/catalog/full.sql")
public class VisualiseCatalogTestMockMvc {

    @Autowired
    private WebClient webClient;

    private HtmlPage index, categoryHtmlPage, productHtmlPage, itemHtmlPage;

    private HtmlElement categoryLink, productLink, itemLink;

    /**
     * This tests starts at index page and click on the products, items and item
     * links
     */
    @Test
    public void testWebVisualiseProducts() throws Exception {

        // The test starts at the index page
        index = webClient.getPage("/");
        // @formatter:off
        assertTrue(index.getBody().asNormalizedText().contains(
            "The YAPS Pet Store Demo for Spring Boot is a fictional sample application"
            ));
        // @formatter:on

        // We click on the first link of the index page
        categoryLink = index.getBody().getOneHtmlElementByAttribute("area",
             "href", "/category/view?id=FISH");
        categoryHtmlPage = categoryLink.click();

        String pageText = categoryHtmlPage.getBody().asNormalizedText();
        assertAll(
            () -> assertTrue(pageText.contains("Liste de produits appartenant à la catégorie Fish")),
            () -> assertTrue(pageText.contains("Angelfish")),
            () -> assertTrue(pageText.contains("Tiger Shark")),
            () -> assertTrue(pageText.contains("Freshwater fish from Japan"))
        );
    }

    @Test
    public void testWebVisualiseItems() throws Exception {

        // The test starts at the index page
        index = webClient.getPage("/");

        // We click on the first link of the index page
        categoryLink = index.getBody().getOneHtmlElementByAttribute("area",
                "href", "/category/view?id=BIRDS");

        categoryHtmlPage = categoryLink.click();

        // We click on the first link of the products page
        try {
            String linkTarget = "/product/view?id=AVCB01";
            productLink = categoryHtmlPage.getBody().getOneHtmlElementByAttribute(
                    "a", "href", linkTarget);
        } catch (ElementNotFoundException e) {
            fail("Expected a link to " + "/product/view?id=AVCB01");
        }
        productHtmlPage = productLink.click();
        assertTrue(productHtmlPage.getBody().asNormalizedText()
                .contains("Liste des items appartenant au produit Amazon Parrot"));
    }

    @Test
    public void testWebVisualiseOneItem() throws Exception {

        // The test starts at the index page
        index = webClient.getPage("/");

        // We click on the first link of the index page
        categoryLink = index.getBody().getOneHtmlElementByAttribute(
                "area", "href",
                "/category/view?id=BIRDS");
        categoryHtmlPage = categoryLink.click();

        // We click on the first link of the products page
        productLink = categoryHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href",
                "/product/view?id=AVCB01");
        productHtmlPage = productLink.click();
        
        // We click on the first link of the items page
        String target = "/item/view?id=EST25";
        try {
        itemLink = productHtmlPage.getBody().getOneHtmlElementByAttribute(
            "a", "href", target);
        } catch (ElementNotFoundException e) {
            fail("Expecting link to item "+ target);
        }
        itemHtmlPage = itemLink.click();
        String itemPageText = itemHtmlPage.getBody().asNormalizedText();

        assertAll(
            () -> assertContains("Amazon Parrot", itemPageText),
            () -> assertContains("Male Adult", itemPageText),
            () -> assertContains("120", itemPageText)
        );
        try {
            itemLink = itemHtmlPage.getBody().getOneHtmlElementByAttribute(
                "img", "src", "/img/bird2.jpg");
            } catch (ElementNotFoundException e) {
                fail("expected image");
            }
    }

    @Test
    public void testWebVisualiseOneItemCAT() throws Exception {

        // The test starts at the index page
        index = webClient.getPage("/");

        // We click on the first link of the index page
        categoryLink = index.getBody().getOneHtmlElementByAttribute(
                "area", "href",
                "/category/view?id=CATS");
        categoryHtmlPage = categoryLink.click();

        // We click on the first link of the products page
        productLink = categoryHtmlPage.getBody().getOneHtmlElementByAttribute("a", "href",
                "/product/view?id=FLDSH01");
        productHtmlPage = productLink.click();
        
        // We click on the first link of the items page
        String target = "/item/view?id=EST23";
        try {
        itemLink = productHtmlPage.getBody().getOneHtmlElementByAttribute(
            "a", "href", target);
        } catch (ElementNotFoundException e) {
            fail("Expecting link to item "+ target);
        }
        itemHtmlPage = itemLink.click();
        String itemPageText = itemHtmlPage.getBody().asNormalizedText();

        assertAll(
            () -> assertContains("Manx", itemPageText),
            () -> assertContains("Male Adult", itemPageText),
            () -> assertContains("120", itemPageText)
        );
        try {
            itemLink = itemHtmlPage.getBody().getOneHtmlElementByAttribute(
                "img", "src", "/img/cat1.jpg");
            } catch (ElementNotFoundException e) {
                fail("expected image");
            }
    }

    private void assertContains(String expected, String computed) {
        assertTrue(computed.contains(expected), "Page text should contain "+ expected);
    }

}
