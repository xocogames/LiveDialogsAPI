package com.xocogames.utils.files;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    /**
     * Devuelve un <code>String</code> con el contenido de un fichero a partir de un nombre de fichero completo con su path en disco.
     * @param fname Nombre completo del fichero.
     * @return String con el contenido del fichero.
     */
    public static String readFile(String fname) throws IOException {
        FileInputStream istream = new FileInputStream(fname);
        return readInputStreamFile(istream);
    }

    /**
     * Devuelve un <code>String</code> con el contenido de un fichero a partir de un nombre de fichero
     * que debe encontrarse en la carpeta "<code>resources</code>" del proyecto.
     * @param fname Nombre del fichero.
     * @return String con el contenido del fichero.
     */
    public static String readResourceFile(String fname) throws IOException {
        InputStream istream = FileUtils.class.getClassLoader().getResourceAsStream(fname);
        return readInputStreamFile(istream);
    }

    /**
     * Devuelve un <code>String</code> con el contenido de un fichero a partir de un InputStream de fichero.
     * @param istream  <code>InputStream</code> del fichero a leer.
     * @return String con el contenido del fichero.
     */
    public static String readInputStreamFile(InputStream istream) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (istream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return textBuilder.toString();
    }}
