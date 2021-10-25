# @package 
#
#       plugin.rb
#
#       Copyright(c) Jatun 2008
#
#       Author: Jatun
#       Created: J   10/1/2008 10:31:18 AM
#
# @package 
#
#       plugin.rb
#
#       Copyright(c) Jatun 2008
#
#       Author: Jatun
#       Created: J   10/1/2008 10:31:16 AM
#
# @package 
#
#       plugin.rb
#
#       Copyright(c) Jatun 2008
#
#       Author: Jatun
#       Created: J   10/1/2008 10:31:13 AM
#
# @package 
#
#       plugin.rb
#
#       Copyright(c) Jatun 2008
#
#       Author: Jatun
#       Created: J   10/1/2008 10:18:11 AM
#
# @package 
#
#       plugin.rb
#
#       Copyright(c) Jatun 2008
#
#       Author: Jatun
#       Created: J   10/1/2008 10:15:28 AM
#
# @package 
#
#       plugin.rb
#
#       Copyright(c) Jatun 2008
#
#       Author: Jatun
#       Created: J   10/1/2008 10:11:19 AM
#
# @package 
#
#       plugin.rb
#
#       Copyright(c) Jatun 2008
#
#       Author: Jatun
#       Created: J   9/4/2008 10:59:32 AM
#
# @package 
#
#       plugin.rb
#
#       Copyright(c) Jatun 2008
#
#       Author: Jatun
#       Created: J   9/4/2008 10:46:18 AM
#
# @package 
#
#       plugin.rb
#
#       Copyright(c) Jatun 2008
#
#       Author: Jatun
#       Created: J   9/4/2008 10:45:22 AM
#
require "erb"
require 'net/http'
require 'iconv'

Field = Struct.new("Field",:id, :idSector, :idName, :idKey, :key, :resource)
class Plugin


  FIELDS_XML = "fields fields_address fields_person fields_employee fields_company fields_action variable templatebar fields_campaign fields_invoice"
  FIELDS_RES = "application toolbar formConfig formSend msg assistent"


  FIELDS = FIELDS_XML + " " + FIELDS_RES
  RESOURCE_FILE_NAMES = "ApplicationResources.properties ApplicationResources_es.properties ApplicationResources_de.properties ApplicationResources_fr.properties"
  RESOURCE_FILE_EXTENSION = "properties"
  ANOTHER_RESOURCES_KEY = "es de fr"
  RESOURCE_KEY_SEPARATOR = "_"



  def initialize()
    @size  = Hash.new(nil)
    @sector = Hash.new(nil)
    @mapFields = Hash.new(nil)

    _sector = 0
    FIELDS_RES.each(' '){|x| 
      x=x.strip
      @sector[x] = _sector
      _sector = _sector + 100
    }
    #  puts @sector.keys
  end

  def getFields(key)
    return @mapFields[key]
  end

  def getSector(key)
    return @sector[key]
  end


  def incSize(key)
    if not @size.has_key?(key) then
      @size[key]=0
    end
    @size[key]= @size[key] + 1
    return @size[key]
  end


  def main()

    allFields = Hash.new(nil)    
    english=1000
    spanish=3000
    german=5000
    french=7000
    languages = Hash.new()
    languages["english"]=english
    languages["spanish"]=spanish
    languages["german"]=german
    languages["french"]=french

    listFields = Hash.new(nil)
    sector = 0
    count = 0
    RESOURCE_FILE_NAMES.each(' ') { |file|

      file = file.strip
      lang=file
      lang=lang.gsub(/[\w]*_((?:\w){2})\.properties/,'\1')
      languageId = english

      print "LANG:#{lang}\n"
      case lang	 	
      when "es"	 	
	print "SPANISH\n"
	languageId = spanish
	listFields[spanish] = Hash.new(nil)
	mapFields = listFields[spanish]
      when "de"	 	
	print "GERMAN\n"
	languageId = german
	listFields[german] = Hash.new(nil)
	mapFields = listFields[german]
      when "fr"	 	
	print "FRENCH\n"
	languageId = french
	listFields[french] = Hash.new(nil)
	mapFields = listFields[french]
      else 
	print "ENGLISH\n"
	listFields[english] = Hash.new(nil)
	mapFields = listFields[english]
	lang=nil
      end
      print "Reading |#{file}|\n"

      sector = 0
      count = 0
      e1=""
      e2=""
      @size  = Hash.new(nil)
      fields = Hash.new(nil)
      firstTime = true
      File.open(file).each_line { |line| 
	FIELDS.each(' '){|s|
	  s=s.strip
	  resourceKey = "plugin."
	  if line.include? "template." then
	    resourceKey = "template."
	  end
	  
	  if ( line =~Regexp.new("#{resourceKey}#{s}.(\\w*)\\s?=\\s?(.*)"))
	    e1 = $1
	    e2 = $2

	    if !mapFields.has_key?(s) then 
	      mapFields[s]= Hash.new(nil) 
	    end
	    idName="#{e1.upcase.strip}" + (lang.nil? ? "" : "_#{lang.upcase.strip}")

	    if languageId == english 
	      allFields[idName]= ""
	    else
	      auxLang = lang.upcase.strip
	      generateResources = true
	      keyResource = e1.upcase.strip
	      if auxLang == "DE" || auxLang == "ES"
		generateResources = allFields.has_key?(keyResource)
	      end 
	      if auxLang == "FR"
	        generateResources = allFields.has_key?(keyResource)
	      end
	      
      	      puts "LANG:#{auxLang} -> #{keyResource} <> #{generateResources} -- #{allFields.has_key?(keyResource)}"
	      if !generateResources
		next
	      end
	    end

	    sector = getSector(s)
	    
	    count = incSize(s)


	    fields = mapFields[s]
	    
	    field = Field.new()
	    keyVar=line.gsub(/([\w.]*)\s*=\s?.*\s?/,'\1')
	    field.idSector = (sector.nil? ? 0 : sector) + count
	    field.id= value = field.idSector + languageId
	    field.key=keyVar.strip
	    field.idName=idName
	    field.idKey="#{e1.upcase.strip}"
	    field.resource=e2.strip.gsub('"', "'")
	    fields[field.idName]=field
	    FIELDS_RES.each(' '){|v|
	      if v.include? s then
		break
	      end
	    }
	    break
	  end
	}
      }
      puts "-------------------------------------------------"
    }   
    












    if @size['fields'] > 0 then 
      erb=ERB.new( File.open( "resources2.template" ).read )
      new_code = erb.result( binding )
      print "Creating resources.template\n"
      File.open( "resources.rs", "w" ).write( new_code )

      erb=ERB.new( File.open( "xmlResources.template" ).read )
      new_code = erb.result( binding )
      print "Creating XMLResources_en.xml \n"
      File.open( "XMLResources_en.xml", "w" ).write(Iconv.iconv('UTF-8', 'LATIN1', new_code))

      erb=ERB.new( File.open( "xmlResources_es.template" ).read )
      new_code = erb.result( binding )
      print "Creating XMLResources_es.xml \n"
      File.open( "XMLResources_es.xml", "w" ).write( Iconv.iconv('UTF-8', 'LATIN1', new_code) )

      erb=ERB.new( File.open( "xmlResources_de.template" ).read )
      new_code = erb.result( binding )
      print "Creating XMLResources_de.xml \n"
      File.open( "XMLResources_de.xml", "w" ).write( Iconv.iconv('UTF-8', 'LATIN1', new_code) )

      erb=ERB.new( File.open( "xmlResources_fr.template" ).read )
      new_code = erb.result( binding )
      print "Creating XMLResources_fr.xml \n"
      File.open( "XMLResources_fr.xml", "w" ).write( Iconv.iconv('UTF-8', 'LATIN1', new_code) )

      erb=ERB.new( File.open( "ConstantModule.template" ).read )
      new_code = erb.result( binding )
      print "Creating ConstantModule.bas \n"
      File.open( "ConstantModule.bas", "w" ).write( new_code )
    end
  end

  def test
    s= "employee"
    line = "plugin.employee.variable = test"
    puts (line =~Regexp.new("plugin.#{s}.(\\w*)\\s?=\\s?(.*)$"))
  end

end


p = Plugin.new()
p.main

