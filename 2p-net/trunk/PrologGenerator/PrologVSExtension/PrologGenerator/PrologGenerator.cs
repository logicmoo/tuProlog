
/***************************************************************************

Copyright (c) Microsoft Corporation. All rights reserved.
This code is licensed under the Visual Studio SDK license terms.
THIS CODE IS PROVIDED *AS IS* WITHOUT WARRANTY OF
ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING ANY
IMPLIED WARRANTIES OF FITNESS FOR A PARTICULAR
PURPOSE, MERCHANTABILITY, OR NON-INFRINGEMENT.

***************************************************************************/

using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Linq;
using System.Runtime.InteropServices;
using System.IO;
using System.Text;
using Microsoft.VisualStudio.Shell;
using VSLangProj80;

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
            StringReader stringReader = new StringReader(inputFileContent);
            List<string> righe = new List<string>();
            string tmp;
            while ((tmp = stringReader.ReadLine()) != null)
            {
                righe.Add(tmp);
            }
            
            string header = Generated.ClassTemplate;
            try
            {

                this.HasError = false;
                // set base content
                string className = Path.GetFileNameWithoutExtension(base.InputFilePath);
                string nameSpace = base.FileNameSpace;
                

                // search and set optional params
                //todo: do as should be done
                
                try
                {
                    nameSpace = righe.Where(riga => riga.StartsWith("%namespace", true, null)).First().Substring(10);                    
                }
                catch (Exception e)
                {
                    this.GeneratorError(4, "Error in NameSpace definition", 1, 1);
                }

                try{
                className = righe.Where(riga => riga.StartsWith("%classname", true, null)).First().Substring(10);
                }
                catch (Exception e)
                {
                    this.GeneratorError(4, "Error in NameSpace definition", 1, 1);
                }

                header = header.Replace("#NameSpace", nameSpace);
                header = header.Replace("#ClassName", className);

                // set the theory
                //todo: remove c# oriented params
                header = header.Replace("#Theory", inputFileContent);

                StringWriter writer = new StringWriter(new StringBuilder());

                writer.Write(header);    
                writer.Flush();

                //Get the Encoding used by the writer. We're getting the WindowsCodePage encoding, 
                //which may not work with all languages
                Encoding enc = Encoding.GetEncoding(writer.Encoding.WindowsCodePage);
                    
                //Get the preamble (byte-order mark) for our encoding
                byte[] preamble = enc.GetPreamble();
                int preambleLength = preamble.Length;
                    
                //Convert the writer contents to a byte array
                byte[] body = enc.GetBytes(writer.ToString());

                //Prepend the preamble to body (store result in resized preamble array)
                Array.Resize<byte>(ref preamble, preambleLength + body.Length);
                Array.Copy(body, 0, preamble, preambleLength, body.Length);
                    
                //Return the combined byte array
                return this.HasError ? null : preamble;
            }
            catch (Exception e)
            {
                this.GeneratorError(4, e.ToString(), 1, 1);
                //Returning null signifies that generation has failed);
                return null;
            }
        }
    }
}