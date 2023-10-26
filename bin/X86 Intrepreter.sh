#!/bin/bash

#Get Txt File Contents
txtFolder="C:/Users/Public/"
txtFile="Windhunter's Programs - Main Folder Location.txt"
programDirectory=$(cat $txtFolder"$txtFile")

#Load the Program
directory="$programDirectory/X86-Interpreter/bin/X86-Interpreter Program.jar"
java -jar "$directory"
