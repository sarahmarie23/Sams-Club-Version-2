import requests
from bs4 import BeautifulSoup

def get_item_details(url):
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'html.parser')

    # Find the image and title based on the HTML structure
    title = soup.find('img', class_='sc-pc-image-controller')['alt']
    image_url = soup.find('img', class_='sc-pc-image-controller')['src']

    return title, image_url
