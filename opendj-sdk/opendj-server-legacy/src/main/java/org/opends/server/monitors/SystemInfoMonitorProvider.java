/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at legal-notices/CDDLv1_0.txt
 * or http://forgerock.org/license/CDDLv1.0.html.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at legal-notices/CDDLv1_0.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2006-2010 Sun Microsystems, Inc.
 *      Portions Copyright 2012-2015 ForgeRock AS.
 */
package org.opends.server.monitors;

import static org.opends.server.util.ServerConstants.*;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;

import org.forgerock.i18n.slf4j.LocalizedLogger;
import org.forgerock.opendj.config.server.ConfigException;
import org.opends.server.admin.std.server.SystemInfoMonitorProviderCfg;
import org.opends.server.api.MonitorProvider;
import org.opends.server.core.DirectoryServer;
import org.opends.server.types.Attribute;
import org.opends.server.types.AttributeBuilder;
import org.opends.server.types.AttributeType;
import org.opends.server.types.Attributes;
import org.opends.server.types.InitializationException;

/**
 * This class defines a Directory Server monitor provider that can be used to
 * collect information about the system and the JVM on which the Directory
 * Server is running.
 */
public class SystemInfoMonitorProvider
       extends MonitorProvider<SystemInfoMonitorProviderCfg>
{
  private static final LocalizedLogger logger = LocalizedLogger.getLoggerForThisClass();

  @Override
  public void initializeMonitorProvider(
                   SystemInfoMonitorProviderCfg configuration)
         throws ConfigException, InitializationException
  {
    // No initialization is required.
  }

  @Override
  public String getMonitorInstanceName()
  {
    return "System Information";
  }

  @Override
  public List<Attribute> getMonitorData()
  {
    ArrayList<Attribute> attrs = new ArrayList<>(13);

    attrs.add(createAttribute("javaVersion",
                              System.getProperty("java.version")));
    attrs.add(createAttribute("javaVendor", System.getProperty("java.vendor")));
    attrs.add(createAttribute("jvmVersion",
                              System.getProperty("java.vm.version")));
    attrs.add(createAttribute("jvmVendor",
                              System.getProperty("java.vm.vendor")));
    attrs.add(createAttribute("javaHome",
                              System.getProperty("java.home")));
    attrs.add(createAttribute("classPath",
                              System.getProperty("java.class.path")));
    attrs.add(createAttribute("workingDirectory",
                              System.getProperty("user.dir")));

    String osInfo = System.getProperty("os.name") + " " +
                    System.getProperty("os.version") + " " +
                    System.getProperty("os.arch");
    attrs.add(createAttribute("operatingSystem", osInfo));
    String sunOsArchDataModel = System.getProperty("sun.arch.data.model");
    if (sunOsArchDataModel != null)
    {
      String jvmArch = sunOsArchDataModel;
      if (! sunOsArchDataModel.toLowerCase().equals("unknown"))
      {
        jvmArch += "-bit";
      }
      attrs.add(createAttribute("jvmArchitecture", jvmArch));
    }
    else
    {
      attrs.add(createAttribute("jvmArchitecture","unknown"));
    }

    try
    {
      attrs.add(createAttribute("systemName",
                     InetAddress.getLocalHost().getCanonicalHostName()));
    }
    catch (Exception e)
    {
      logger.traceException(e);
    }


    Runtime runtime = Runtime.getRuntime();
    attrs.add(createAttribute("availableCPUs",
                              String.valueOf(runtime.availableProcessors())));
    attrs.add(createAttribute("maxMemory",
                              String.valueOf(runtime.maxMemory())));
    attrs.add(createAttribute("usedMemory",
                              String.valueOf(runtime.totalMemory())));
    attrs.add(createAttribute("freeUsedMemory",
                              String.valueOf(runtime.freeMemory())));
    String installPath = DirectoryServer.getServerRoot();
    if (installPath != null)
    {
      attrs.add(createAttribute("installPath", installPath));
    }
    String instancePath = DirectoryServer.getInstanceRoot();
    if (instancePath != null)
    {
      attrs.add(createAttribute("instancePath", instancePath));
    }

    // Get the JVM input arguments.
    RuntimeMXBean rtBean = ManagementFactory.getRuntimeMXBean();
    List<String> jvmArguments = rtBean.getInputArguments();
    if ((jvmArguments != null) && (! jvmArguments.isEmpty()))
    {
      StringBuilder argList = new StringBuilder();
      for (String jvmArg : jvmArguments)
      {
        if (argList.length() > 0)
        {
          argList.append(" ");
        }

        argList.append("\"");
        argList.append(jvmArg);
        argList.append("\"");
      }

      attrs.add(createAttribute("jvmArguments", argList.toString()));
    }

    // Get the list of supported SSL protocols and ciphers.
    Collection<String> supportedTlsProtocols;
    Collection<String> supportedTlsCiphers;
    try
    {
      final SSLContext context = SSLContext.getDefault();
      final SSLParameters parameters = context.getSupportedSSLParameters();
      supportedTlsProtocols = Arrays.asList(parameters.getProtocols());
      supportedTlsCiphers = Arrays.asList(parameters.getCipherSuites());
    }
    catch (Exception e)
    {
      // A default SSL context should always be available.
      supportedTlsProtocols = Collections.emptyList();
      supportedTlsCiphers = Collections.emptyList();
    }

    addAttribute(attrs, ATTR_SUPPORTED_TLS_PROTOCOLS, supportedTlsProtocols);
    addAttribute(attrs, ATTR_SUPPORTED_TLS_CIPHERS, supportedTlsCiphers);

    return attrs;
  }

  private void addAttribute(ArrayList<Attribute> attrs, String attrName, Collection<String> values)
  {
    AttributeType attrType = DirectoryServer.getDefaultAttributeType(attrName);
    AttributeBuilder builder = new AttributeBuilder(attrType);
    builder.addAllStrings(values);
    attrs.add(builder.toAttribute());
  }

  /**
   * Constructs an attribute using the provided information.  It will have the
   * default syntax.
   *
   * @param  name   The name to use for the attribute.
   * @param  value  The value to use for the attribute.
   *
   * @return  The attribute created from the provided information.
   */
  private Attribute createAttribute(String name, String value)
  {
    AttributeType attrType = DirectoryServer.getDefaultAttributeType(name);
    return Attributes.create(attrType, value);
  }
}

