Notas para la actualizacion del editor TinyMCE a una version mas reciente.

* Descargar la version mas reciente desde su sitio: http://tinymce.moxiecode.com/download.php
* Descargar las traducciones de lenguaje (Language packs) necesarios para la correspondiente version: http://tinymce.moxiecode.com/download.php
* Copiar las traducciones de lenguaje en sus correspondientes directorios (el 'Language packs' define la ruta donde se deben copiar las traducciones)
* Reemplazar todo el directorio "tinymce", renombrar el directorio 'tinymce/jscripts/tiny_mce' por la correspondiente version q se esta actualizando. Ejemplo 'tinymce/jscripts/tiny_mce_3_0_8'
* Actualizar la url del script "tiny_mce.js" en 'initTinyMCEditor.tag'
* Actualizar la url del script "tiny_mce_popup.js" en 'ImageBrowse.jsp'
* DEFINIR UN FONT SIZE POR DEFECTO A 10pt: No es posible configurar de forma sencilla el tama�o por defecto de la fuente,
    lo q sugieren es crear un CSS q sobreescriba la clase del BODY y configurar este archivo en un atributo de
    configuracion (content_css) para q sea cargado. Esta configuracion funciona correctamente en firefox pero no
    asi en IExplorer (este navegador siempre dando problemas), por esta razon se modifico directamente el archivo
    de estilo del propio TinyMCE. El archivo modificado se encuentra en:

       "tinymce\jscripts\tiny_mce\{themes}\advanced\{skins}\default\content.css"

    TinyMCE permite configurar diferentes {themes}, y para cada theme diferentes {skins}, cada uno de estos tiene
    un archivo de estilo CSS diferente. Entonces si se quiere configurar un theme diferente o un skin diferente,
    se debe ir a modificar el estilo "content.css" correspondiente para definir un font size por defecto. En nuestro
    caso se edito el archivo de estilos "content.css" del skin por defecto del editor.
* Testear y verificar el correcto funcionamiento del editor + plugin ELWIS.
