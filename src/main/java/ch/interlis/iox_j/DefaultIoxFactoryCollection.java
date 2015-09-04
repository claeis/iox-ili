/* This file is part of the iox-ili project.
 * For more information, please see <http://www.eisenhutinformatik.ch/iox-ili/>.
 *
 * Copyright (c) 2006 Eisenhut Informatik AG
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package ch.interlis.iox_j;

import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxFactory;
import ch.interlis.iox.IoxFactoryCollection;

import java.util.ArrayList;


/**
 * This class provides a default implementation for the IoxFactoryCollection
 * interface.
 *
 * @author ceis
 */
public class DefaultIoxFactoryCollection implements IoxFactoryCollection {

    private final ArrayList<IoxFactory> factoryv = new ArrayList<IoxFactory>();


    public DefaultIoxFactoryCollection() throws IoxException {
        registerFactory(new GenericIoxFactory());
    }


    @Override
    public void registerFactory(IoxFactory factory) throws IoxException {
        if (factory == null) {
            throw new IoxException("Null factory");
        }
        factoryv.add(0, factory);
    }


    @Override
    public void removeFactory(IoxFactory factory) throws IoxException {
        factoryv.remove(factory);
    }


    @Override
    public IomObject createIomObject(String type, String oid) throws IoxException {
        for (int f = 0, s = factoryv.size(); f < s; ++f) {
            IomObject iomObj = factoryv.get(f).createIomObject(type, oid);

            if (iomObj != null) {
                return iomObj;
            }
        }
        return null;
    }

}
