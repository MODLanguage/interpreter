/*
 * MIT License
 *
 * Copyright (c) 2020 NUM Technology Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

// Generated from /Users/tonywalmsley/work/num/grammar/antlr4/MODLParser.g4 by ANTLR 4.7.2
package uk.modl.parser.antlr;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link MODLParserVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public class MODLParserBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements MODLParserVisitor<T> {

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public T visitModl(MODLParser.ModlContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public T visitModl_structure(MODLParser.Modl_structureContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public T visitModl_map(MODLParser.Modl_mapContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public T visitModl_array(MODLParser.Modl_arrayContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public T visitModl_pair(MODLParser.Modl_pairContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public T visitModl_value(MODLParser.Modl_valueContext ctx) {
    return visitChildren(ctx);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * The default implementation returns the result of calling
   * {@link #visitChildren} on {@code ctx}.
   * </p>
   */
  @Override
  public T visitModl_primitive(MODLParser.Modl_primitiveContext ctx) {
    return visitChildren(ctx);
  }

}