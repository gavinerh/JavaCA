using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static System.Console;

namespace Playground
{
    class exerciseC
    {
        static void Main()
        {
            Menu();
        }

        static void Menu()
        {
            WriteLine("Exercise C for 11.8.2021\nInput 1/2/3/4/5 to get to corresponding exercises, Enter to terminate.");
            string uinputMenu = ReadLine();
            switch (uinputMenu)
            {
                case "1":
                    A1();
                    break;

                case "2":
                    A2();
                    break;

                case "3":
                    A3();
                    break;

                case "4":
                    A4();
                    break;

                case "5":
                    A5();
                    break;

                case "6":
                    A6();
                    break;
            }
        }

        private static void A1()
        {
            WriteLine("What is your name?\n");
            string uName = ReadLine();
            WriteLine("What is your Gender?? M/F\n");
            string uGender = ReadLine();
            uGender = uGender.ToLower();
            switch (uGender)
            {
                case "m":
                    WriteLine($"Good Day, Mr.{uName}\n");
                    Menu();
                    break;

                case "f":
                    WriteLine($"Good Day, Ms.{uName}\n");
                    Menu();
                    break;

                default:
                    WriteLine("Please enter a valid input");
                    A1();
                    break;
            }
        }

        private static void A2()
        {
            WriteLine("What is your name?\n");
            string uName = ReadLine();
            WriteLine("What is your Gender?? M/F\n");
            string uGender = ReadLine();
            uGender = uGender.ToLower();
            WriteLine("How old are you?\n");
            bool check = int.TryParse(ReadLine(), out int uAge);
            if (check == false)
            {
                WriteLine("Please enter a valid age in numbers");
                A2();
            }
            switch (uGender)
            {
                case "m":
                    if (uAge > 40)
                    {
                        WriteLine($"Good Day, Uncle {uName}\n");
                        Menu();
                        break;
                    }
                    else
                    {
                         WriteLine($"Good Day, Mr. {uName}\n");
                         Menu();
                         break;
                    }
                case "f":
                    if (uAge > 40)
                    {
                        WriteLine($"Good Day, Auntie {uName}\n");
                        Menu();
                        break;
                    }
                    else
                    {
                        WriteLine($"Good Day, Ms. {uName}\n");
                        Menu();
                        break;
                    }
                default:
                    WriteLine("Please enter a valid input");
                    A2();                 
                    break;
            }
        }

        private static void A3()
        {
            WriteLine("Input marks between 0 and 100");
            bool check = int.TryParse(ReadLine(), out int uMark);
            if (check)
            {
                if (uMark > 100 || uMark < 0)
                {
                    WriteLine("Please enter a mark between 0 and 100.\n");
                    Menu();
                }
                else
                {
                    string uGrade = "";
                    if (uMark >= 80)
                    {
                        uGrade = "A";
                    }
                    else if (uMark >= 60)
                    {
                        uGrade = "B";
                    }
                    else if (uMark >= 40)
                    {
                        uGrade = "C";
                    }
                    else
                    {
                        uGrade = "F";
                    }
                    WriteLine($"You scored {uMark} which is {uGrade} grade.\n");
                    ReadKey();
                    Menu();
                }
            }
            else
            {
                WriteLine("Please enter a valid mark in numbers\n");
                Menu();
            }
        }

        private static void A4()
        {
            double minCharge = 2.4;
            double stage1Charge = 0.04;
            double stage1Limit = 0.5;
            double stage2Charge = 0.05;
            double stage2Limit = 8.5;
            double tCharge = 0;
            WriteLine("Taxi charge calculator: Enter distance in km");
            bool check = double.TryParse(ReadLine(), out double distKm);
            if (check)
            {
                double distKmRounded = ((Math.Floor(distKm * 10 + 1 )) / 10);
                tCharge = tCharge + minCharge;
                if (distKmRounded >= stage1Limit)
                {
                    tCharge = tCharge + ((distKmRounded - stage1Limit) * stage1Charge);
                }

                if (distKmRounded > stage2Limit)
                {
                    tCharge = tCharge +  ((distKmRounded - stage1Limit - stage2Limit) * stage2Charge);
                }
                WriteLine($"The total charge is ${tCharge:0.0}.\n");
                ReadKey();
                Menu();
            }
            else
            {
                WriteLine("Please enter a valid number. \n");
                A4();
            }
        }

        private static void A5()
        {

        }

        private static void A6()
        {
            WriteLine("What is your name?\n");
            string uName = ReadLine();
            WriteLine("What is your Gender?? M/F\n");
            string uGender = ReadLine();
            uGender = uGender.ToLower();
            WriteLine("How old are you?\n");
            bool check = int.TryParse(ReadLine(), out int uAge);       
            if (check == false)
            {
                WriteLine("Please enter a valid age in numbers");
                A6();
            }
            string title = "";
            switch (uGender)
            {
                case "m":
                    if (uAge > 40)
                    {
                        title = "Uncle";
                        break;
                    }
                    else
                    {
                        title = "Mr.";
                        break;
                    }
                case "f":
                    if (uAge > 40)
                    {
                        title = "Auntie";
                        break;
                    }
                    else
                    {
                        title = "Ms.";
                        break;
                    }
                default:
                    WriteLine("Please enter a valid input");
                    A6();
                    break;
            }
            WriteLine($"Good Day, {title} {uName}!\n");
            ReadKey();
            Menu();
        }
    }
}


