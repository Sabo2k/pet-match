#!/usr/bin/env python3
"""
Test script for Advertisement CRUD endpoints.
"""
import requests
import json

BASE_URL = "http://localhost:8080/api/v1"
session = requests.Session()

def test_get_all_advertisements():
    """Test GET all advertisements (public endpoint)"""
    print("=" * 50)
    print("TEST 1: GET all advertisements (public)")
    print("=" * 50)
    response = session.get(f"{BASE_URL}/advertisements")
    print(f"Status: {response.status_code}")
    print(f"Response: {response.json()}\n")
    return response.status_code == 200

def test_login():
    """Test login to get authentication cookie"""
    print("=" * 50)
    print("TEST 2: Login")
    print("=" * 50)
    data = {
        "email": "user@test.com",
        "password": "password"
    }
    response = session.post(f"{BASE_URL}/auth/login", json=data)
    print(f"Status: {response.status_code}")
    print(f"Response: {response.json()}")
    print(f"Cookies: {session.cookies.get_dict()}\n")
    return response.status_code == 200

def test_create_advertisement():
    """Test creating an advertisement (authenticated)"""
    print("=" * 50)
    print("TEST 3: Create advertisement (authenticated)")
    print("=" * 50)
    print(f"Cookies before request: {session.cookies.get_dict()}")
    data = {
        "title": "Beautiful Siberian Husky",
        "description": "2 years old, friendly, loves running",
        "age": 2,
        "price": 750.0,
        "location": "Los Angeles"
    }
    response = session.post(f"{BASE_URL}/advertisements", json=data)
    print(f"Status: {response.status_code}")
    print(f"Response headers: {dict(response.headers)}")
    if response.status_code == 201:
        ad = response.json()
        print(f"Response: {json.dumps(ad, indent=2)}")
        return ad
    else:
        print(f"Response: {response.text}\n")
        return None

def test_get_single_advertisement(ad_id):
    """Test getting a single advertisement"""
    print("=" * 50)
    print("TEST 4: GET single advertisement")
    print("=" * 50)
    response = session.get(f"{BASE_URL}/advertisements/{ad_id}")
    print(f"Status: {response.status_code}")
    if response.status_code == 200:
        print(f"Response: {json.dumps(response.json(), indent=2)}\n")
        return True
    else:
        print(f"Response: {response.text}\n")
        return False

def test_update_advertisement(ad_id):
    """Test updating an advertisement"""
    print("=" * 50)
    print("TEST 5: UPDATE advertisement")
    print("=" * 50)
    data = {
        "id": ad_id,
        "title": "Beautiful Siberian Husky - UPDATED",
        "description": "2 years old, friendly, loves running - now available!",
        "age": 2,
        "price": 650.0,
        "location": "Los Angeles"
    }
    response = session.put(f"{BASE_URL}/advertisements/{ad_id}", json=data)
    print(f"Status: {response.status_code}")
    if response.status_code == 200:
        print(f"Response: {json.dumps(response.json(), indent=2)}\n")
        return True
    else:
        print(f"Response: {response.text}\n")
        return False

def test_delete_advertisement(ad_id):
    """Test deleting an advertisement"""
    print("=" * 50)
    print("TEST 6: DELETE advertisement")
    print("=" * 50)
    response = session.delete(f"{BASE_URL}/advertisements/{ad_id}")
    print(f"Status: {response.status_code}")
    if response.status_code == 204:
        print("Advertisement deleted successfully\n")
        return True
    else:
        print(f"Response: {response.text}\n")
        return False

def test_get_all_advertisements_final():
    """Test GET all advertisements again"""
    print("=" * 50)
    print("TEST 7: GET all advertisements (after delete)")
    print("=" * 50)
    response = session.get(f"{BASE_URL}/advertisements")
    print(f"Status: {response.status_code}")
    print(f"Response: {response.json()}\n")
    return response.status_code == 200

if __name__ == "__main__":
    print("\n🚀 Starting Advertisement CRUD Tests\n")
    
    try:
        # Test 1: Get all (public)
        test_get_all_advertisements()
        
        # Test 2: Login
        if not test_login():
            print("❌ Login failed!")
            exit(1)
        
        # Test 3: Create
        ad = test_create_advertisement()
        if not ad:
            print("❌ Create failed!")
            exit(1)
        
        ad_id = ad["id"]
        
        # Test 4: Get single
        if not test_get_single_advertisement(ad_id):
            print("❌ Get single failed!")
            exit(1)
        
        # Test 5: Update
        if not test_update_advertisement(ad_id):
            print("❌ Update failed!")
            exit(1)
        
        # Test 6: Delete
        if not test_delete_advertisement(ad_id):
            print("❌ Delete failed!")
            exit(1)
        
        # Test 7: Get all final
        test_get_all_advertisements_final()
        
        print("✅ All tests passed!")
        
    except Exception as e:
        print(f"❌ Error: {e}")
        exit(1)
