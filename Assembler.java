package Assemble;

import java.util.*;
import java.io.*;
import Parser.*;
import Lex.*;

public class Assembler
{
   private Quads [] quad;
   private Symbol [] sym;
   
   public Assembler(Quads [] q, Symbol [] s) throws FileNotFoundException, IOException
   {
      quad = q;
      sym = s;
      processAssemble();
   }
   
   private void processAssemble() throws IOException
   {
      BufferedWriter bw = new BufferedWriter(new FileWriter("code.asm"));
      bw.write("");
      bw = null;
      bw = new BufferedWriter(new FileWriter("code.asm",true));
      String save = "\r\n";
      String data = "";
      String arg1, arg2, arg3;
      String symbol;
      
      // initialize statement
      data += "%include 'IO.asm'" + save;
      data += "Section .data" + save;
      bw.write(data);
      data = "";
      // process data section
      for (int i = 0; i < sym.length; i++)
      {
         if (sym[i].getSegment().equals("DS"))
         {
            arg1 = sym[i].getToken();
            arg2 = sym[i].getValue();
            if (arg2.equals("?"))
            {
               data += arg1 + " resw 1" + save;
            }
            else
            {
               bw.write(arg1 + " DW " + arg2 + save);
            }
         }
      }
      // initialize .bss section
      bw.write("section .bss" + save);
      bw.write(data);
      bw.write("global  _start" + save);
      data = "";
      // initialize code section
      data += "Section .code" + save;
      data += "_start:  nop" + save;
      bw.write(data);
      data = "";
      
      // process code section
      for (int i = 0; i < quad.length; i++)
      {
         symbol = quad[i].getSym();
         arg1 = quad[i].getArg1();
         arg2 = quad[i].getArg2();
         arg3 = quad[i].getArg3();
         
         switch (symbol)
         {
            case "READ":
               data += "call PrintString" + save;
               data += "call GetAnInteger" + save;
               data += "mov  eax,[ReadInt]" + save;
               data += "mov  [" + arg1 + "],eax" + save;
               break;
            case "WRITE":
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "call ConvertIntegerToString" + save;
               data += "mov  eax, 4" + save;
               data += "mov  ebx, 1" + save;
               data += "mov  ecx, Result" + save;
               data += "mov  edx, ResultEnd" + save;
               data += "int 80h" + save;
               break;
            case "=":
               data += "mov  eax,[" + arg2 + "]" + save;
               data += "mov  [" + arg1 + "],eax" + save;
               break;
            case "+":
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "add  eax,[" + arg2 + "]" + save;
               data += "mov  [" + arg3 + "],eax" + save;
               break;
            case "-":
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "sub  eax,[" + arg2 + "]" + save;
               data += "mov  [" + arg3 + "],eax" + save;
               break;
            case "/":
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "idiv word[" + arg2 + "]" + save;
               data += "mov  [" + arg3 + "],eax" + save;
               break;
            case "*":
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "imul word[" + arg2 + "]" + save;
               data += "mov  [" + arg3 + "],eax" + save;
               break;
            case "%":
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "idiv word[" + arg2 + "]" + save;
               data += "mov  [" + arg3 + "],edx" + save;
               break;
            case "==":
               data += arg3 + ": nop" + save;
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "cmp  eax,[" + arg2 + "]" + save;
               data += "jne  end" + arg3 + save;
               break;
            case ">":
               data += arg3 + ": nop" + save;
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "cmp  eax,[" + arg2 + "]" + save;
               data += "jle  end" + arg3 + save;
               break;
            case "<":
               data += arg3 + ": nop" + save;
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "cmp  eax,[" + arg2 + "]" + save;
               data += "jge  end" + arg3 + save;
               break;
            case ">=":
               data += arg3 + ": nop" + save;
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "cmp  eax,[" + arg2 + "]" + save;
               data += "jl   end" + arg3 + save;
               break;
            case "<=":
               data += arg3 + ": nop" + save;
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "cmp  eax,[" + arg2 + "]" + save;
               data += "jg   end" + arg3 + save;
               break;
            case "!=":
               data += arg3 + ": nop" + save;
               data += "mov  eax,[" + arg1 + "]" + save;
               data += "cmp  eax,[" + arg2 + "]" + save;
               data += "je   end" + arg3 + save;
               break;
            case "IF":
               data += "end" + arg1 + ": nop" + save;
               break;
            case "WHILE":
               data += "j  " + arg1 + save;
               data += "end" + arg1 + ": nop" + save;
               break;
            default:
               continue;
         }
         bw.write(data + save);
         data = "";
      }
      data += "mov  ah,04Ch" + save;
      data += "xor  al,al" + save;
      data += "int  21h" + save;
      bw.write(data);
      bw.close();
   }
}