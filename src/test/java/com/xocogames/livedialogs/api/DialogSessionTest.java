package com.xocogames.livedialogs.api;


import org.junit.jupiter.api.Test;

import java.util.List;

class DialogSessionTest {

    @Test
    public void testRegexp() {
        List<String> listVarNames;

        listVarNames = DialogSession.resolverVarsExpressions("Mi nombre es ${nombrecito} del carayu");
        System.out.println("Var names:");
        for (String varName: listVarNames) {
            System.out.println(" - "+ varName);
        }
    }
}