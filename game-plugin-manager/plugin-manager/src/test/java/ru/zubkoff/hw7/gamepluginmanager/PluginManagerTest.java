package ru.zubkoff.hw7.gamepluginmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


class PluginManagerTest {
  
  PluginManager pluginManager = new PluginManager(".\\src\\test\\resources\\plugins");
  
  @Test
  void givenPlugin_whenLoadPlugin_thenLoadedPluginWorks() throws ClassNotFoundException {
    //given
    var pluginName = "helloworldplugin";
    var pluginClassName = "plugins.helloworldplugin.HelloWorldPlugin";

    //when
    Plugin helloWorldPlugin = pluginManager.load(pluginName, pluginClassName);

    //then
    assertEquals("Hello World!", helloWorldPlugin.doUsefull());
  }
  
  @Test
  void givenClassWhichNotImplemensPlugin_whenLoadPlugin_thenThrowNotAPluginException() throws ClassNotFoundException {
    //given
    var pluginName = "notapluginimplementation";
    var pluginClassName = "plugins.notapluginimplementation.NotAPluginImplementation";

    //when then
    assertThrows(
      NotAPluginException.class, 
      () -> pluginManager.load(pluginName, pluginClassName)
    );
  }
  
  @Test
  void givenNotExistingPluginClassFile_whenLoadPlugin_thenThrowClassNotFoundException() throws ClassNotFoundException {
    //given
    var pluginName = "helloworldplugin";
    var pluginClassName = "plugins.helloworldplugin.NotExistingPluginClassFile";

    //when then
    assertThrows(
      ClassNotFoundException.class, 
      () -> pluginManager.load(pluginName, pluginClassName)
    );
  }

}
