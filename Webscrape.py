# -*- coding: utf-8 -*-
"""
Spyder Editor
This is a temporary script file.
"""

# -*- coding: utf-8 -*-
"""
Spyder Editor
This is a temporary script file.
"""

# -*- coding: utf-8 -*-
"""
Spyder Editor
This is a temporary script file.
"""

# -*- coding: utf-8 -*-
"""
Created on Fri Aug 13 13:54:39 2021
@author: lib-pac-olin-ppc
"""
#imports 

from contextlib import nullcontext
from os import link
from selenium import webdriver
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.alert import Alert
from bs4 import BeautifulSoup as bs
import pandas as pd
import re
import math
import lxml
import random
import numpy as np
import time
#pd.set_option('display.max_rows', None)
#pd.set_option('display.max_columns', None)
#pd.set_option('display.width', None)
#pd.set_option('display.max_colwidth', -1)

import sys
!{sys.executable} -m pip install selenium


#SET INDEX TO THE ROW WITH THE FIRST EMPTY SMILE FOR THE POLYMER (THE LOOP WILL START AT THAT ROW)
index = 2318

df = pd.read_excel('C:/Users/major/Downloads/Melting_data_with_MOL_FILE.xlsx', skiprows = index)
df.columns = ["C1", "C2", "PolymerID","Polymer_Name","Polymer_ID","Polymer_Class","Mol_File_SMILES","Number_Of_Samples","Melting_Temperature","Monomer_1.0","Monomer_1.1","Polymerization_type_1","Reaction_1","SMILES_1.0","SMILES_1.1","Monomer_2.0","Monomer_2.1","Reaction_2","SMILES_2.0","SMILES_2.1","Polymerization_type_2","Monomer_3.0","Monomer_3.1","Reaction_3","SMILES_3.0","SMILES_3.1","Polymerization_type_3","Monomer_4.0","Monomer_4.1","Reaction_4","Unnamed: 42","SMILES_4","Polymerization_type_4","Monomer_5.0","Monomer_5.1","Reaction_5","SMILES_5.0","SMILES_5.1","Polymerization_type_5","Monomer_6.0","Monomer_6.1","Reaction_6","SMILES_6","Polymerization_type_6","Monomer_7.0","Monomer_7.1","Reaction_7","SMILES_7","Polymerization_type_7","Monomer_8.0","Monomer_8.1","Reaction_8","SMILES_8","Polymerization_type_8","Monomer_9.0","Monomer_9.1","Reaction_9","SMILES_9","Polymerization_type_9","Monomer_10.1","Reaction_10","SMILES_10"]
df = df.drop(df.columns[list(range(9,62))], axis=1)
df = df.drop(df.columns[5], axis = 1)

polymerIDs = df["Polymer_ID"].tolist()
df.set_index('C1', inplace=True)


login_url="https://mdpf-cas.nims.go.jp/cas/login?service=https%3a%2f%2fpolymer.nims.go.jp%2fPoLyInfo%2fcgi-bin%2fp-search.cgi"

driver = webdriver.Chrome('C:/Users/major/Downloads/chromedriver_win32/chromedriver.exe')

from selenium.webdriver.chrome.webdriver import WebDriver as ChromeDriver

def login():
    driver.get(login_url)

    #target username
    email = WebDriverWait(driver, 10).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "input[name='username']")))
    password = WebDriverWait(driver, 10).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "input[name='password']")))

    #enter username and password
    email.clear()
    email.send_keys("lthrwqfddkotdrpbgy@adfskj.com")
    password.clear()
    password.send_keys("EpicGamer123!")

    #target the login button and click it
    button = WebDriverWait(driver, 2).until(EC.element_to_be_clickable((By.CSS_SELECTOR, "button[type='submit']"))).click()

def getSmiles(molFile):
    driver.switch_to.window(handles[1])
    WebDriverWait(driver, 5).until(EC.element_to_be_clickable((By.XPATH, "/html/body/nav/div/div[2]/ul[1]/li[4]/a"))).click()
    WebDriverWait(driver, 5).until(EC.element_to_be_clickable((By.XPATH, "/html/body/nav/div/div[2]/ul[1]/li[4]/ul/li[3]/a"))).click()
    textbox = driver.find_element_by_xpath("/html/body/div[7]/div/div/div[2]/textarea")
    time.sleep(1)
    textbox.send_keys(molFile)
    driver.find_element_by_xpath("/html/body/div[7]/div/div/div[3]/button[1]").click()
    time.sleep(1.5)
    WebDriverWait(driver, 5).until(EC.element_to_be_clickable((By.XPATH, "/html/body/nav/div/div[2]/ul[1]/li[4]/a"))).click()
    driver.find_element_by_xpath("/html/body/nav/div/div[2]/ul[1]/li[4]/ul/li[9]/a").click()
    time.sleep(3)
    text = driver.find_element_by_xpath("/html/body/div[8]/div/div/div[2]/textarea").get_attribute("value");
    driver.find_element_by_xpath("/html/body/div[8]/div/div/div[3]/button[2]").click()
    driver.switch_to.window(handles[0])
    
    return text    

login()

time.sleep(2)

handles = driver.window_handles

driver.execute_script("window.open('about: blank', 'secondtab');")
driver.switch_to.window("secondtab")
driver.get("https://chemdrawdirect.perkinelmer.cloud/js/sample/index.html#")
button = WebDriverWait(driver, 5).until(EC.element_to_be_clickable((By.XPATH, "/html/body/div[3]/div/div[6]/div/div/div[5]/div/div/div[2]/div/div/div[3]/button[1]"))).click()

handles = driver.window_handles

driver.switch_to.window(handles[0])

time.sleep(1)

for polymerID in polymerIDs:
    urlHeader1 = polymerID[1:3]
    url = "https://polymer.nims.go.jp/PoLyInfo/cgi-bin/mols/" + urlHeader1 + "/" + polymerID + ".mol"
    driver.get(url)
    time.sleep(5)
    handles = driver.window_handles
    if("requested URL" in driver.page_source):
        index = index + 1
        continue
    molFile = driver.find_element_by_xpath("/html/body/pre").text
    smile = getSmiles(molFile)
    df.loc[index, 'Mol_File_SMILES'] = smile
    index = index + 1

'''
df = pd.merge(df, monomerDataFrame, on='index')
#monomerDataFrame.to_csv("Melting Temp_3.csv",encoding = 'utf-8',header = True)
df.to_csv('Meltingtemp_trial_starting_page_start_134_end_142_remaining.csv', encoding='utf-8', header=True)
#df.to_csv("Melting_temp.csv")
'''
