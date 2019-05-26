/*
 * Copyright (c) 2015, Franco Toninato. All rights reserved.
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW. 
 * EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER 
 * PARTIES PROVIDE THE PROGRAM �AS IS� WITHOUT WARRANTY OF ANY KIND, EITHER 
 * EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE 
 * QUALITY AND PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE 
 * DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR CORRECTION.
 */
package it.tnnfnc.security.algorithm;


/**
 * This class implements cipher parameters with an Initialization Vector.
 *
 * @author      Franco Toninato
 * @version     %I%, %G%
 * @since       1.2
 */
public class IVAlgorithmParameter  implements I_AlgorithmParameter
{
    private byte[] iv;
    
    /**
     * Class constructors. 
     * Add an IV to existing parameters.
     */
    public IVAlgorithmParameter(byte[] iv)
    {
        this(iv, 0, iv.length);
    }

    /**
     * Class constructors. 
     * Add an IV to existing parameters.
     */
    public IVAlgorithmParameter(byte[] iv, int ivOff, int ivLen)
    {
        this.iv = new byte[ivLen];
        System.arraycopy(iv, ivOff, this.iv, 0, ivLen);
    }

    /**
     * Return the IV. 
     */
    public byte[] getIV()
    {
        return iv;
    }
    
}
