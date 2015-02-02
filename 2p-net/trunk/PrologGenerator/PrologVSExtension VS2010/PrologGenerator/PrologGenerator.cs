using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.IO;
using System.Text;
using Microsoft.VisualStudio.Shell;
using VSLangProj80;
using EnvDTE;

namespace PrologGenerator
{
    /// <summary>
    /// This is the generator class. 
    /// When setting the 'Custom Tool' property of a C# or VB project item to "PrologGenerator", 
    /// the GenerateCode function will get called and will return the contents of the generated file 
    /// to the project system
    /// </summary>
    [ComVisible(true)]
    [Guid("52B316AA-1997-4c81-9969-83604C09EEB4")]
    [CodeGeneratorRegistration(typeof(PrologGenerator),"C# Prolog Class Generator",vsContextGuids.vsContextGuidVCSProject,GeneratesDesignTimeSource=true)]
    [CodeGeneratorRegistration(typeof(PrologGenerator), "VB Prolog Class Generator", vsContextGuids.vsContextGuidVBProject, GeneratesDesignTimeSource = true)]
    [ProvideObject(typeof(PrologGenerator))]
    public class PrologGenerator : BaseCodeGeneratorWithSite
    {
#pragma warning disable 0414
        //The name of this generator (use for 'Custom Tool' property of project item)
        internal static string name = "PrologGenerator";
#pragma warning restore 0414


        /// <summary>
        /// Function that builds the contents of the generated file based on the contents of the input file
        /// </summary>
        /// <param name="inputFileContent">Content of the input file</param>
        /// <returns>Generated file as a byte array</returns>
        protected override byte[] GenerateCode(string inputFileContent)
        {

            Project project = this.GetProject();
            if (project.FullName.EndsWith("csproj"))
                return GenerateCSharpCode(inputFileContent);
            else
                return GenerateVBCode(inputFileContent);
        }
        
        private byte[] GenerateCSharpCode(string inputFileContent)
        {
            StringReader stringReader = new StringReader(inputFileContent);
            List<string> rows = new List<string>();
            List<string> potentialParam = new List<string>();
            string tmp;
            while ((tmp = stringReader.ReadLine()) != null)
            {
                if(tmp.StartsWith("%"))
                    potentialParam.Add(tmp.Trim());
                rows.Add(tmp);
            }

           
            try
            {
                this.HasError = false;
                
                // Set optional params if not found set default Value
                string nameSpace, className, extFile;

                extFile = GetValue("%extfile", potentialParam, "");
                nameSpace = GetValue("%namespace", potentialParam, base.GetProject().Name);
                className = GetValue("%classname", potentialParam, Path.GetFileNameWithoutExtension(base.InputFilePath));
                

                string template;
                // if no extfile defined
                if (extFile == "")
                {
                    template = FileResource.CSharpTemplate;
                    template = template.Replace("#NameSpace", nameSpace.Trim());
                    template = template.Replace("#ClassName", className.Trim());

                    // copy and set theory inside template row by row
                    string theory = rows.Aggregate("", (current, row) => current + (row + "\r\n"));
                    template = template.Replace("#Theory", theory);
                }
                else // extFile defined
                {
                    template = FileResource.CSharpExternalTemplate;
                    template = template.Replace("#NameSpace", nameSpace.Trim());
                    template = template.Replace("#ClassName", className.Trim());
                    template = template.Replace("#ExtFile", extFile.Trim());
                }

                MemoryStream ms = new MemoryStream();
                StreamWriter writer = new StreamWriter(ms, new UTF8Encoding());
                writer.Write(template);    
                writer.Flush();
                ms.Position = 0;
                
                return this.HasError ? null : ms.ToArray();
            }
            catch (Exception e)
            {
                this.GeneratorError(4, e.ToString(), 1, 1);
                //Returning null signifies that generation has failed);
                return null;
            }
        }

        private byte[] GenerateVBCode(string inputFileContent)
        {
            StringReader stringReader = new StringReader(inputFileContent);
            List<string> rows = new List<string>();
            List<string> potentialParam = new List<string>();
            string tmp;
            while ((tmp = stringReader.ReadLine()) != null)
            {
                if (tmp.StartsWith("%"))
                    potentialParam.Add(tmp.Trim());
                else
                    rows.Add(tmp);
            }


            try
            {
                this.HasError = false;

                // Set optional params if not found set default Value
                string nameSpace, className, extFile;

                extFile = GetValue("%extfile", potentialParam, "");
                nameSpace = GetValue("%namespace", potentialParam, base.GetProject().Name);
                className = GetValue("%classname", potentialParam, Path.GetFileNameWithoutExtension(base.InputFilePath));


                string template;
                // if no extfile defined
                if (extFile == "")
                {
                    template = FileResource.VBTemplate;
                    template = template.Replace("#NameSpace", nameSpace.Trim());
                    template = template.Replace("#ClassName", className.Trim());

                    // copy and set theory inside template row by row
                    string theory = rows.Aggregate("", (current, row) => current + (row + "\r\n"));
                    template = template.Replace("#Theory", theory);
                }
                else // extFile defined
                {
                    template = FileResource.VBExternalTemplate;
                    template = template.Replace("#NameSpace", nameSpace.Trim());
                    template = template.Replace("#ClassName", className.Trim());
                    template = template.Replace("#ExtFile", extFile.Trim());
                }

                MemoryStream ms = new MemoryStream();
                StreamWriter writer = new StreamWriter(ms, new UTF8Encoding());
                writer.Write(template);
                writer.Flush();
                ms.Position = 0;

                return this.HasError ? null : ms.ToArray();
            }
            catch (Exception e)
            {
                this.GeneratorError(4, e.ToString(), 1, 1);
                //Returning null signifies that generation has failed);
                return null;
            }
        }


        private string GetValue(string keyword, List<string> rows, string defaultValue)
        {
            string tmp = defaultValue;
            try
            {

                if (rows.Where(row => row.StartsWith(keyword, true, null)).Count() != 0)
                {
                    tmp = rows.Where(row => row.StartsWith(keyword, true, null)).First().Split(new char[]{' '}).Last();
                    rows.RemoveAll(row => row.StartsWith(keyword, true, null));
                }

            }
            catch (Exception e)
            {
                this.GeneratorError(4, "Error in "+keyword+" definition", 1, 1);
            }
            return tmp;
        }
    }
}