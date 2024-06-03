package com.example.scene3;

public class cgpaCalculation {

    private  float cgpa= 0.0F;

     public float  calculate_cgpa(int points , int bonusPoints , int damagePoints)
    {
        long total_points=points/9;
        if(total_points>=80)
        {
            cgpa=4.00F;
        }
        else if(total_points>=75)
        {
            cgpa=3.75F;
        }
        else if(total_points>=70)
        {
            cgpa=3.50F;
        } else if (total_points>=65)
        {
            cgpa=3.25F;
        }
        else if(total_points>=60)
        {
            cgpa=3.00F;
        }
        else if(total_points>=55)
        {
            cgpa=2.75F;
        }
        else if(total_points>=50)
        {
            cgpa=2.50F;
        }
        else
        {
            cgpa=0.00F;
        }


        return  cgpa;
    }
}
