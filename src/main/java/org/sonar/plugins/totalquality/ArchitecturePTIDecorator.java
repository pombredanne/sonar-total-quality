/*
 * Sonar Total Quality Plugin
 * Copyright (C) 2010 Martin (e72636) and Emilio Escobar Reyero (escoem)
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.totalquality;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.MeasureUtils;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Resource;
import org.sonar.api.utils.ParsingUtils;

public class ArchitecturePTIDecorator extends AbstractBaseDecorator {

  @DependedUpon
  public List<Metric> generatesMetrics() {
    return Arrays.asList(TQMetrics.TQ_ARCHITECTURE_PTI);
  }

  @DependsUpon
  public List<Metric> dependsOnMetrics() {
    return Arrays.asList(CoreMetrics.PACKAGE_TANGLE_INDEX, CoreMetrics.FILE_TANGLE_INDEX, CoreMetrics.NCLOC);
  }

  @Override
  void decorateFile(Resource resource, DecoratorContext context) {
  }

  @Override
  void decorateDir(Resource resource, DecoratorContext context) {
    doDecoration(resource, context, CoreMetrics.FILE_TANGLE_INDEX);
  }

  private void doDecoration(Resource resource, DecoratorContext context, Metric metric) {
    final Measure measure = context.getMeasure(metric);
    final double pti = MeasureUtils.hasValue(measure) ? (100 - measure.getValue().doubleValue()) : 100D;

    context.saveMeasure(TQMetrics.TQ_ARCHITECTURE_PTI, ParsingUtils.scaleValue(pti, 2));
  }

  @Override
  void decorateProj(Resource resource, DecoratorContext context) {
    doDecoration(resource, context, CoreMetrics.PACKAGE_TANGLE_INDEX);
  }

}
