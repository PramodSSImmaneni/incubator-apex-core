/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.datatorrent.stram.api;

import com.datatorrent.api.Operator;
import com.datatorrent.stram.plan.logical.LogicalPlanConfiguration;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class PropertyChange implements ConfigurationChange
{
  private String name;
  private String value;

  private PropertyChange()
  {
    //For Kryo
  }

  public PropertyChange(String propertyName, String propertyValue)
  {
    this.name = Preconditions.checkNotNull(propertyName);
    this.value = Preconditions.checkNotNull(propertyValue);
  }

  public String getName()
  {
    return name;
  }

  public String getValue()
  {
    return value;
  }

  public static void applyPropertyChanges(Collection<ConfigurationChange> configurationChanges, Operator operator)
  {
    if (configurationChanges == null || configurationChanges.isEmpty()) {
      return;
    }

    Map<String, String> properties = Maps.newHashMap();

    for (ConfigurationChange configurationChange : configurationChanges) {
      if (configurationChange instanceof PropertyChange) {
        PropertyChange propertyChange = (PropertyChange)configurationChange;
        properties.put(propertyChange.getName(), propertyChange.getValue());
      }
    }

    if (properties.isEmpty()) {
      return;
    }

    LogicalPlanConfiguration.setOperatorProperties(operator, properties);
  }
}
