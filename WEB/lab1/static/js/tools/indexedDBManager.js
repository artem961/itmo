class DBManager {
    constructor() {
        this.db = null;
        const request = indexedDB.open("lab1", 1);

        request.onerror = (e) => {
            console.error("DB connection error:", e.stack);
        }

        request.onsuccess = (e) => {
            this.db = e.target.result;
        }

        request.onupgradeneeded = (e) => {
            const db = e.target.result;
            if (!db.objectStoreNames.contains('items')) {
                db.createObjectStore('items', { autoIncrement: true });
            }
        }
    }

    async addItem(item) {
        if (!this.db) await this.waitForDB();

        return new Promise((resolve, reject) => {
            const transaction = this.db.transaction('items', 'readwrite');
            const store = transaction.objectStore('items');

            const request = store.add({
                data: item,
                timestamp: new Date().toISOString()
            });

            request.onsuccess = () => resolve(request.result);
            request.onerror = () => reject(request.error);
        });
    }

    async getAllItems() {
        if (!this.db) await this.waitForDB();

        return new Promise((resolve, reject) => {
            const transaction = this.db.transaction('items', 'readonly');
            const store = transaction.objectStore('items');
            const request = store.getAll();

            request.onsuccess = () => resolve(request.result);
            request.onerror = () => reject(request.error);
        });
    }

    async deleteAllItems() {
        if (!this.db) await this.waitForDB();

        return new Promise((resolve, reject) => {
            const transaction = this.db.transaction('items', 'readwrite');
            const store = transaction.objectStore('items');
            const request = store.clear();

            request.onsuccess = () => resolve(request.result);
            request.onerror = () => reject(request.error);
        })
    }

    waitForDB() {
        return new Promise(resolve => {
            if (this.db) resolve();
            else setTimeout(() => this.waitForDB().then(resolve), 100);
        });
    }
}