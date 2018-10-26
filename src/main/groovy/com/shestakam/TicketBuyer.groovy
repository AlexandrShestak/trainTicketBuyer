package com.shestakam

import com.codeborne.selenide.Selenide
import org.openqa.selenium.By

import static com.codeborne.selenide.Selenide.$
import static com.codeborne.selenide.Selenide.open

class TicketBuyer {
    static void main(String[] args) {
        Properties prop = new Properties()
        InputStream input = TicketBuyer.class.getClassLoader().getResourceAsStream("config.properties")
        prop.load(new InputStreamReader(input, "UTF-8"))

        open("https://poezd.rw.by/wps/portal/home/login_main/")

        //login
        $('input#login').value = prop.getProperty('login')
        $('input#password').value = prop.getProperty('password')
        $(".commandExButton").click()
        sleep(2000)

        //accept site rules
        $(By.xpath("//*[@id=\"viewns_Z7_9HD6HG80NGMO80ABJ9NPD12001_:form1:conf\"]")).click()
        sleep(2000)


        //from, to, date
        $(By.xpath("//*[@id=\"viewns_Z7_9HD6HG80NGMO80ABJ9NPD12001_:form1:textDepStat\"]")).value = prop.getProperty('from')
        $(By.xpath("//*[@id=\"viewns_Z7_9HD6HG80NGMO80ABJ9NPD12001_:form1:textArrStat\"]")).value = prop.getProperty('to')
        $(By.xpath("//*[@id=\"viewns_Z7_9HD6HG80NGMO80ABJ9NPD12001_:form1:dob\"]")).value =  prop.getProperty('date')
        $(By.xpath("//*[@id=\"viewns_Z7_9HD6HG80NGMO80ABJ9NPD12001_:form1:buttonSearch\"]")).click()
        sleep(2000)


        //find train by time
        boolean trainFounded = false
        while (!trainFounded) {
            for (int i = 1; ; i++) {
                def element = $(By.xpath("//*[@id=\"viewns_Z7_9HD6HG80NGMO80ABJ9NPD12001_:form2:tableEx1:tbody_element\"]/tr[${i}]"))

                if (element.exists()) {
                    String time = element.find(By.xpath("./td[5]")).getText()

                    if (time == prop.getProperty('time')) {
                        element.find(By.xpath("./td[1]")).click()
                        trainFounded = true
                        break
                    }
                } else {
                    Selenide.sleep(Integer.parseInt(prop.getProperty('refreshInterval')))
                    Selenide.refresh()
                    break
                }
            }
        }


        // this code is needed to not close browser
        Scanner scan = new Scanner(System.in)
        scan.nextLine()
    }
}
