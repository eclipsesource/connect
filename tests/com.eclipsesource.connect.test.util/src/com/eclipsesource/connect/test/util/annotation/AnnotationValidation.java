/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation, ongoing development
 ******************************************************************************/
package com.eclipsesource.connect.test.util.annotation;

import java.lang.annotation.Annotation;


public class AnnotationValidation<A extends Annotation> {

  private A annotation;

  AnnotationValidation( A annotation ) {
    this.annotation = annotation;
  }

  public AnnotationValidation<A> and( AnnotationValidator<A> validator ) {
    validator.validate( annotation );
    return this;
  }

}
