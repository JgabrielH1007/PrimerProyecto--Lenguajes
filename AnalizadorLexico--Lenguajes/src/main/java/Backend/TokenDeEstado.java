/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

/**
 *
 * @author gabrielh
 */
public class TokenDeEstado {
    private final String TOKENHTML = ">>[html]";
    private final String TOKENCSS = ">>[css]";
    private final String TOKENJAVASCRIPT = ">>[js]";
    
    public boolean esTokenDeEstado(String token){
        return token.equals(TOKENHTML)|| token.equals(TOKENCSS)|| token.equals(TOKENJAVASCRIPT);
    }
    
    public String getTOKENHTML() {
        return TOKENHTML;
    }

    public String getTOKENCSS() {
        return TOKENCSS;
    }

    public String getTOKENJAVASCRIPT() {
        return TOKENJAVASCRIPT;
    }
    
    
}
