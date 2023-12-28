package com.rebrickable.unit.lego;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.rebrickable.Rebrickable;
import com.rebrickable.lego.ColorService;
import com.rebrickable.lego.model.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8001)
public class ColorServiceTest {

    private static ColorService SERVICE;

    @BeforeAll
    public static void initService() {
        SERVICE = new Rebrickable("", "http://localhost:8001")
                .lego()
                .color();
    }

    @Test
    void testPage() throws IOException {
        stubFor(get("/lego/colors/?page=1&page_size=1").willReturn(jsonResponse("{\n" +
                "  \"count\": 123,\n" +
                "  \"next\": \"http://localhost:8001/lego/colors/?page=2&page_size=1\",\n" +
                "  \"previous\": null,\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"id\": -1,\n" +
                "      \"name\": \"[Unknown]\",\n" +
                "      \"rgb\": \"0033B2\",\n" +
                "      \"is_trans\": false,\n" +
                "      \"external_ids\": {\n" +
                "        \"BrickOwl\": {\n" +
                "          \"ext_ids\": [\n" +
                "            0\n" +
                "          ],\n" +
                "          \"ext_descrs\": [\n" +
                "            [\n" +
                "              \"Not Applicable\"\n" +
                "            ]\n" +
                "          ]\n" +
                "        },\n" +
                "        \"LEGO\": {\n" +
                "          \"ext_ids\": [\n" +
                "            125\n" +
                "          ],\n" +
                "          \"ext_descrs\": [\n" +
                "            [\n" +
                "              \"Light Orange\"\n" +
                "            ]\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}", 200)));

        List<Color> colors = SERVICE.page(1, 1);
        assertThat(colors).isNotNull();
        assertThat(colors).hasSize(1);
        Color color = colors.get(0);
        assertThat(color).isNotNull();
        assertThat(color.name).isEqualTo("[Unknown]");
        assertThat(color.externalIds).hasSize(2);
    }

    @Test
    void testAll() throws IOException {
        stubFor(get("/lego/colors/").willReturn(jsonResponse("{\n" +
                "  \"count\": 2,\n" +
                "  \"next\": \"http://localhost:8001/lego/colors/?page=2\",\n" +
                "  \"previous\": null,\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"id\": -1,\n" +
                "      \"name\": \"[Unknown]\",\n" +
                "      \"rgb\": \"0033B2\",\n" +
                "      \"is_trans\": false,\n" +
                "      \"external_ids\": {\n" +
                "        \"BrickOwl\": {\n" +
                "          \"ext_ids\": [\n" +
                "            0\n" +
                "          ],\n" +
                "          \"ext_descrs\": [\n" +
                "            [\n" +
                "              \"Not Applicable\"\n" +
                "            ]\n" +
                "          ]\n" +
                "        },\n" +
                "        \"LEGO\": {\n" +
                "          \"ext_ids\": [\n" +
                "            125\n" +
                "          ],\n" +
                "          \"ext_descrs\": [\n" +
                "            [\n" +
                "              \"Light Orange\"\n" +
                "            ]\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}", 200)));

        stubFor(get("/lego/colors/?page=2").willReturn(jsonResponse("{\n" +
                "  \"count\": 2,\n" +
                "  \"next\": null,\n" +
                "  \"previous\": \"http://localhost:8001/lego/colors/?page_size=1\",\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"Black\",\n" +
                "      \"rgb\": \"05131D\",\n" +
                "      \"is_trans\": false,\n" +
                "      \"external_ids\": {\n" +
                "        \"BrickLink\": {\n" +
                "          \"ext_ids\": [\n" +
                "            11\n" +
                "          ],\n" +
                "          \"ext_descrs\": [\n" +
                "            [\n" +
                "              \"Black\"\n" +
                "            ]\n" +
                "          ]\n" +
                "        },\n" +
                "        \"BrickOwl\": {\n" +
                "          \"ext_ids\": [\n" +
                "            38\n" +
                "          ],\n" +
                "          \"ext_descrs\": [\n" +
                "            [\n" +
                "              \"Black\"\n" +
                "            ]\n" +
                "          ]\n" +
                "        },\n" +
                "        \"LEGO\": {\n" +
                "          \"ext_ids\": [\n" +
                "            26\n" +
                "          ],\n" +
                "          \"ext_descrs\": [\n" +
                "            [\n" +
                "              \"Black\",\n" +
                "              \"BLACK\"\n" +
                "            ]\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}", 200)));

        List<Color> colors = SERVICE.all();
        assertThat(colors).isNotNull();
        assertThat(colors).hasSize(2);
    }


}
