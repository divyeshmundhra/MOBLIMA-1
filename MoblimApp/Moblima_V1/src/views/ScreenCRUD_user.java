/* Note: 
 * This CRUD has been implemented keeping admin in mind
 * For User a separate CRUD for screen will exist
 * In this CRUD you can add show times
 * I the other user CRUD you will be able to book seats		
 * Also proof of concept of object serialization
 * 			
 * */
package views;
import models.*;


import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;

public class ScreenCRUD_user extends ScreenCRUD{
	
public static void main(String[] args) throws Exception {
		
		int choice = -1;
		Scanner sc = new Scanner(System.in);
		
		ScreenCRUD_user screencrud = new ScreenCRUD_user();
		System.out.println();
		System.out.println("1: View All Show Times");
		System.out.println("2: Book Seats");
		System.out.println("3: Go Back");
		System.out.print("Please enter an option: ");
		System.out.println();
		choice = sc.nextInt();
		
		while(choice > 3 || choice < 1) {
			
			System.out.println();
			System.out.println("1: View All Show Times");
			System.out.println("2: Book Seats");
			System.out.println("Enter 0 to Exit");
			System.out.print("Please enter an option: ");
			System.out.println();
			choice = sc.nextInt();
		}
			if (choice == 1) {
				screencrud.readShowTime();
				ScreenCRUD_user.main(null);
			}
			
			if (choice == 2) {
				screencrud.bookSeats();
				ScreenCRUD_user.main(null);
			}
			if(choice == 3) {
				UserFunctions.main(null);
			}
			
		
			
	}

	public void readShowTime() {
		readScreen();
		
		int i;
		int j;
		for(i=0; i< no_Screen; i++) {
			
			if(arr_Screen[i].get_no_Show_time()!=0) {
				System.out.println("Show Times for Screen ID: "+(i+1)+ " are...");
				
				for(j=0; j< arr_Screen[i].get_no_Show_time(); j++) {
					System.out.print("ID:	"+ arr_Screen[i].arr_Show_time[j].get_ID()+"; ");
					System.out.print("movie: "+ arr_Screen[i].arr_Show_time[j].get_movie()+"; ");
					System.out.print("Date: "+ arr_Screen[i].arr_Show_time[j].get_date()+"; ");
					System.out.print(" Time: "+ arr_Screen[i].arr_Show_time[j].get_time()+"; ");
					System.out.println();
				}
			}
		}
		
		writeScreen();
	}
	
	public void bookSeats() throws ParseException, IOException {
		readScreen();
		
		Scanner sc = new Scanner(System.in);
		int ID, i = 0;
		int row = 0, col = 0;
		int sID;
		boolean flag1 = true;
		boolean flag2 = true;
		System.out.println("Enter Screen id");
		ID = sc.nextInt();
		
		if(ID<=9 && ID>=1) {
			flag1= false;
			System.out.println("Enter Show Time id");
			sID = sc.nextInt();
			for(i=0; i< arr_Screen[ID-1].get_no_Show_time(); i++) {
				if(arr_Screen[ID-1].arr_Show_time[i].get_ID()==sID) {
					flag2= false;
					boolean full= true;
					for (int a = 0; a< arr_Screen[ID-1].arr_Show_time[i].get_numOfRows(); a++) {
						for(int b=0; b< arr_Screen[ID-1].arr_Show_time[i].get_numOfColumns(); b++) {
							if(arr_Screen[ID-1].arr_Show_time[i].seat_layout[a][b].isBooked()==false) {
								full = false;
								break;
							}
							if(!full)
								break;
						}
					}
						
					if(full) {
						System.out.println("This Show Time is all booked, sorry!");
						break;
					}
					
					
					System.out.println("The seat layout is as follows:");
					System.out.println();
					arr_Screen[ID-1].arr_Show_time[i].view_layout_booked();
					System.out.println();
					System.out.println("Enter row");
					row = sc.nextInt();
					System.out.println("Enter column");
					col = sc.nextInt();
					
					if((0<=row&&row<arr_Screen[ID-1].arr_Show_time[i].get_numOfRows())&&(0<=col&&col<arr_Screen[ID-1].arr_Show_time[i].get_numOfColumns())) {   //This part is harcoded for now
						
						if(arr_Screen[ID-1].arr_Show_time[i].view_seat_booked(row,col)) {
							System.out.println("This seat is already booked! Try another seat!");
							i--;
							continue;
						}
						arr_Screen[ID-1].arr_Show_time[i].book_on_click(row, col);
						System.out.println("Now layout is as follows:");
						System.out.println();
						arr_Screen[ID-1].arr_Show_time[i].view_layout_booked();
					}   
					else {
						System.out.println("Sorry seat co-ordinates are out of bounds!");
					}
					
					break;
				}
				
				if(i==arr_Screen[ID-1].get_no_Show_time()) {
					System.out.println("No such show, try again!");
				}
			}
		}
		else {
			System.out.println("Such Screen ID does not exist, try again!");
		}
		
		if(!flag1 && !flag2) {
			Booking book = new Booking(row, col, arr_Screen[ID-1].arr_Show_time[i].get_movie(), arr_Screen[ID-1].get_screenID(), arr_Screen[ID-1].get_screenType(),arr_Screen[ID-1].arr_Show_time[i].get_date(),arr_Screen[ID-1].get_cineplexType());
			book.promptCustomerInformation();
			book.printBookingDetail();
			book.computeTotalPrice();
			writeScreen();
		}
		
	}

}