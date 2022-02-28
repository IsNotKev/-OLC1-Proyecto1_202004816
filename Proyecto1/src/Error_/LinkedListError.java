/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Error_;

import java.util.ArrayList;

/**
 *
 * @author kevin
 */
public class LinkedListError extends ArrayList<Error_>{
    public LinkedListError() {
		super();
	}
	
	public void addError(Error_ error){
		this.add(error);
	}
}
