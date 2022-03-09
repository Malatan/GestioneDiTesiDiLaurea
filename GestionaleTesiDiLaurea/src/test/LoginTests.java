package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import businessLogic.ControllerLogin;

class LoginTests {
	 ControllerLogin login;
	 
	    @BeforeEach                                         
	    void setUp() {
	    	login = new ControllerLogin();
	    	login.disposeView();
	    }
	    
	    @ParameterizedTest
	    @ValueSource(ints = {10000, 10001, 10002, 10003, 10004})                                                   
	    @DisplayName("Tests con credenziali corretti")   
	    void testMultiply(int candidate) { 
	    	assertTrue(login.checkLogin(candidate + "", "123"), () -> "Ok");
	    }

	    @ParameterizedTest
	    @ValueSource(ints = {100000, 100010, 100020, 100030, 100040})                                      
	    @DisplayName("Tests con credenziali errati")
	    void testMultiplyWithZero(int candidate) {
	    	assertFalse(login.checkLogin(candidate + "", "123"), () -> "Ok");
	    }
}
